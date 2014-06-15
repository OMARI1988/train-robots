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
import com.trainrobots.planner.Planner;
import com.trainrobots.scenes.Layout;

public class Parser {

	private final Gss gss = new Gss();
	private final Queue queue;
	private final Grammar grammar;
	private final Lexicon lexicon;
	private final Items<Terminal> tokens;
	private final List<GssVertex> frontier = new ArrayList<GssVertex>();
	private final LinkedList<GssVertex> reductionQueue = new LinkedList<GssVertex>();
	private final ParserFilter filter;
	private final boolean verbose;

	public Parser(Layout layout, Grammar grammar, Items<Losr> items,
			Items<Terminal> tokens, boolean verbose) {
		this(layout, grammar, null, items, tokens, verbose);
	}

	public Parser(Layout layout, Grammar grammar, Lexicon lexicon,
			Items<Losr> items, Items<Terminal> tokens, boolean verbose) {

		this.grammar = grammar;
		this.lexicon = lexicon;
		this.queue = new Queue(items);
		this.tokens = tokens;
		this.filter = new ParserFilter(new Planner(layout), tokens);
		this.verbose = verbose;

		if (verbose) {
			System.out.println("INITIAL");
			printState();
		}
	}

	public Losr parse() {

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
				filter.validateResult(losr);
				valid.add(candidate);
				if (verbose) {
					System.out.println("Valid: " + candidate.losr());
				}
			} catch (Exception exception) {
			}
		}
		if (valid.size() == 0) {
			throw new RoboticException("%d candidates were invalid.",
					trees.size());
		}

		// Rank.
		Candidate best = null;
		for (Candidate candidate : valid) {
			if (best == null || filter.better(candidate, best)) {
				best = candidate;
			}
		}
		Losr result = best.losr();
		if (verbose) {
			System.out.println("Best: " + result);
		}
		return result;
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
		Node[] nodes = { new Node(new Type(Types.Region)),
				new Node(new Type(Types.Reference)) };
		createFrontier(nodes, false);
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
			createFrontier(new Node[] { node }, true);
			return;
		}

		// Terminal.
		Terminal terminal = (Terminal) losr;
		TextContext context = terminal.context();
		String key = key(tokens, context);
		Items<LexicalEntry> entries = lexicon.entries(null, key);
		if (entries == null || entries.count() == 0) {
			throw new RoboticException("Not in lexicon: '" + tokens + "' as "
					+ node);
		}

		// Add.
		Node[] nodes = new Node[entries.count()];
		for (int i = 0; i < entries.count(); i++) {
			LexicalEntry entry = entries.get(i);
			Node mappedNode = new Node(entry.terminal().withContext(context));
			mappedNode.weight(entry.weight());
			nodes[i] = mappedNode;
		}
		createFrontier(nodes, true);
	}

	private void createFrontier(Node[] nodes, boolean clearPrevious) {

		// Clear previous?
		GssVertex[] parents = frontier.toArray(new GssVertex[0]);
		if (clearPrevious) {
			frontier.clear();
		}

		// Add nodes to new frontier, setting parents to the previous frontier.
		for (int i = 0; i < nodes.length; i++) {
			GssVertex vertex = gss.add(nodes[i]);
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
			filter.validatePartial(losr);
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