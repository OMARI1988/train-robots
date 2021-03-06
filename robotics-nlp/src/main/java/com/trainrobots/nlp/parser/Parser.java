/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.parser;

import static com.trainrobots.nlp.lexicon.LexicalKey.key;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.collections.SingleItem;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.losr.factory.LosrFactory;
import com.trainrobots.nlp.grammar.EllipsisRule;
import com.trainrobots.nlp.grammar.Grammar;
import com.trainrobots.nlp.grammar.ProductionRule;
import com.trainrobots.nlp.lexicon.LexicalEntry;
import com.trainrobots.nlp.lexicon.Lexicon;

public class Parser {

	private final Gss gss = new Gss();
	private final Queue queue = new Queue();
	private final List<GssVertex> frontier = new ArrayList<GssVertex>();
	private final LinkedList<GssVertex> reductionQueue = new LinkedList<GssVertex>();
	private final ParserContext context;
	private final Grammar grammar;
	private final Lexicon lexicon;
	private final boolean verbose;

	public Parser(ParserContext context, Grammar grammar, boolean verbose) {
		this(context, grammar, null, verbose);
	}

	public Parser(ParserContext context, Grammar grammar, Lexicon lexicon,
			boolean verbose) {

		this.context = context;
		this.grammar = grammar;
		this.lexicon = lexicon;
		this.verbose = verbose;

		if (verbose) {
			System.out.println("INITIAL");
			printState();
		}
	}

	public Losr parse(Items<Losr> items) {

		// Items.
		queue.add(items);

		// Parse.
		List<Node> trees = shiftReduce();
		if (trees.size() == 0) {
			throw new RoboticException("No parse trees.");
		}

		// Validate.
		List<Candidate> valid = new ArrayList<Candidate>();
		for (Node tree : trees) {
			Candidate candidate = new Candidate(tree);
			new AnaphoraResolver().resolve(candidate.losr());
			Losr losr = candidate.losr();
			try {
				context.validateResult(losr);
				valid.add(candidate);
				if (verbose) {
					System.out.println("Valid: " + candidate.losr());
				}
			} catch (Exception exception) {
				if (verbose) {
					if (losr.span().end() == context.tokens().count()) {
						System.out.print("* ");
					}
					System.out.println(exception.getMessage() + " // "
							+ candidate.losr());
				}
			}
		}
		if (valid.size() == 0) {
			throw new RoboticException("%d candidates were invalid.",
					trees.size());
		}

		// Rank.
		Candidate best = null;
		for (Candidate candidate : valid) {
			if (best == null || context.better(candidate, best)) {
				best = candidate;
			}
		}
		Losr result = best.losr();
		if (verbose) {
			System.out.println("Best: " + result);
		}
		return result;
	}

	public int vertextCount() {
		return gss.vertices().size();
	}

	private List<Node> shiftReduce() {

		// Parse.
		while (!queue.empty()) {
			shift();
			reduce();
			if (ellipsis()) {
				reduce();
			}
		}

		// Results.
		List<Node> results = new ArrayList<Node>();
		for (GssVertex vertex : gss.vertices()) {
			if (vertex.parents().size() == 0) {
				results.add(vertex.node());
			}
		}
		return results;
	}

	private boolean ellipsis() {

		String tag = matchEllipsis();
		if (tag == null) {
			return false;
		}

		// Diagnostics.
		if (verbose) {
			System.out.println();
			System.out.println("ELLIPSIS");
		}

		// Add.
		createFrontier(new ItemsArray(new Node(new Type(Types.Region)),
				new Node(new Type(Types.Reference))), false);
		return true;
	}

	private String matchEllipsis() {

		Node previous = queue.get(-1);
		Node next = queue.get(0);

		if (previous != null) {
			for (EllipsisRule rule : grammar.ellipsisRules()) {
				if (previous.losr().name().equals(rule.before())) {
					if (rule.after() == null && next == null) {
						return rule.tag();
					}
					if (rule.after() != null && next != null
							&& rule.after().equals(next.losr().name())) {
						return rule.tag();
					}
				}
			}
		}

		return null;
	}

	private void shift() {

		// Diagnostics.
		if (verbose) {
			System.out.println();
			System.out.println("SHIFT");
		}

		// Dequeue.
		Node node = queue.read();

		// Parsing partial input?
		Losr losr = node.losr();
		if (lexicon == null) {
			createFrontier(new SingleItem(node), true);
			return;
		}

		// Terminal.
		Terminal terminal = (Terminal) losr;
		TextContext textContext = terminal.context();
		String key = key(context.tokens(), textContext);
		Items<LexicalEntry> entries = lexicon.entries(key);

		// Not in lexicon?
		if (entries == null || entries.count() == 0) {
			return;
		}

		// Add.
		ItemsList<Node> nodes = new ItemsList<>();
		for (int i = 0; i < entries.count(); i++) {
			LexicalEntry entry = entries.get(i);
			if (entry.terminal() != null) {
				Node mappedNode = new Node(entry.terminal().withContext(
						textContext));
				mappedNode.weight(entry.weight());
				nodes.add(mappedNode);
			}
		}
		if (nodes.count() > 0) {
			createFrontier(nodes, true);
		}
	}

	private void createFrontier(Items<Node> nodes, boolean clearPrevious) {

		// Clear previous?
		GssVertex[] parents = frontier.toArray(new GssVertex[0]);
		if (clearPrevious) {
			frontier.clear();
		}

		// Add nodes to new frontier, setting parents to the previous frontier.
		int size = nodes.count();
		for (int i = 0; i < size; i++) {
			GssVertex vertex = gss.add(nodes.get(i));
			for (GssVertex parent : parents) {
				vertex.parents().add(parent);
			}
			frontier.add(vertex);
		}

		// Diagnostics.
		if (verbose) {
			printState();
		}
	}

	private void reduce() {
		if (frontier.size() == 0) {
			throw new RoboticException("Frontier is empty.");
		}
		reductionQueue.clear();
		reductionQueue.addAll(frontier);
		while (!reductionQueue.isEmpty()) {
			reduce(reductionQueue.pop());
		}
	}

	private void reduce(GssVertex top) {
		List<GssVertex> path = new ArrayList<GssVertex>();
		for (ProductionRule rule : grammar.productionRules()) {
			path.clear();
			path.add(top);
			match(rule, 0, path);
		}
	}

	private void match(ProductionRule rule, int step, List<GssVertex> path) {

		// Match.
		int index = rule.count() - 1 - step;
		if (index < 0) {
			return;
		}
		GssVertex vertex = path.get(path.size() - 1);
		String tag = rule.get(index);
		if (!vertex.node().losr().name().equals(tag)) {
			return;
		}
		for (GssVertex parent : vertex.parents()) {
			path.add(parent);
			match(rule, step + 1, path);
			path.remove(path.size() - 1);
		}
		if (index != 0) {
			return;
		}

		// Reduce.
		int size = rule.count();
		Node[] children = new Node[size];
		Losr[] items = new Losr[size];
		for (int i = 0; i < size; i++) {
			children[i] = path.get(size - 1 - i).node();
			items[i] = children[i].losr();
		}
		Losr losr;
		try {
			losr = LosrFactory.build(0, 0, rule.lhs(), new ItemsArray(items));
			context.validatePartial(losr);
		} catch (Exception exception) {
			return;
		}

		// Node.
		Node node = new Node(losr, children);
		GssVertex reduction = gss.add(node);
		if (verbose) {
			System.out.println();
			System.out.println("RULE = " + rule);
			System.out.println("REDUCE = " + reduction);
			System.out.println("SPAN = " + reduction.node().losr().span());
		}
		GssVertex last = path.get(path.size() - 1);
		for (GssVertex parent : last.parents()) {
			reduction.parents().add(parent);
		}
		reductionQueue.push(reduction);
		frontier.add(reduction);
		if (verbose) {
			printState();
		}
	}

	private void printState() {

		// Queue.
		System.out.println("Q = " + queue);

		// Empty stack?
		int size = gss.vertices().size();
		if (size == 0) {
			System.out.println("S = EMPTY");
			return;
		}

		// Vertices.
		for (GssVertex vertex : gss.vertices()) {
			System.out.print("V" + vertex.id());
			if (vertex.parents().size() > 0) {
				System.out.print(" (-->");
				for (GssVertex parent : vertex.parents()) {
					System.out.print(' ');
					System.out.print("V" + parent.id());
				}
				System.out.print(")");
			}
			System.out.println(" = " + vertex);
		}
	}
}