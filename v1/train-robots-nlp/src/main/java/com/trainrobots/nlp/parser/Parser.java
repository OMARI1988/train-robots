/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.nlp.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.csp.Planner;
import com.trainrobots.nlp.parser.grammar.EllipsisRule;
import com.trainrobots.nlp.parser.grammar.Grammar;
import com.trainrobots.nlp.parser.grammar.ProductionRule;
import com.trainrobots.nlp.parser.lexicon.Lexicon;
import com.trainrobots.nlp.parser.lexicon.Mapping;
import com.trainrobots.nlp.parser.lexicon.MappingList;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;

public class Parser {

	private final Gss gss = new Gss();
	private final Planner planner;
	private final Queue queue;
	private final Grammar grammar;
	private final Lexicon lexicon;
	private final List<Node> tokens;
	private final List<GssVertex> frontier = new ArrayList<GssVertex>();
	private final LinkedList<GssVertex> reductionQueue = new LinkedList<GssVertex>();
	private final boolean verbose;

	public Parser(WorldModel world, Grammar grammar, Lexicon lexicon,
			List<Node> items, List<Node> tokens) {
		this(world, grammar, lexicon, items, tokens, false);
	}

	public Parser(WorldModel world, Grammar grammar, Lexicon lexicon,
			List<Node> items, List<Node> tokens, boolean verbose) {
		this.planner = new Planner(world);
		this.verbose = verbose;
		this.grammar = grammar;
		this.lexicon = lexicon;
		this.queue = new Queue(items);
		this.tokens = tokens;

		if (verbose) {
			System.out.println("INITIAL");
			printState();
		}
	}

	public ParserResult parse() {

		// Parse.
		List<Node> trees = shiftReduce();
		if (trees.size() == 0) {
			ParserResult result = new ParserResult("No parse trees.");
			if (verbose) {
				System.out.println(result.reason());
			}
			return result;
		}

		// Validate.
		List<Candidate> valid = new ArrayList<Candidate>();
		List<Candidate> invalid = new ArrayList<Candidate>();
		for (Node tree : trees) {
			Candidate candidate = new Candidate(tree);
			AnaphoraResolver.resolve(candidate.rcl);
			if (planner.isValidEvent(candidate.rcl)) {
				valid.add(candidate);
			} else {
				invalid.add(candidate);
			}
		}
		if (valid.size() == 0) {
			if (verbose) {
				for (Candidate candidate : invalid) {
					System.out.println("Not valid: " + candidate.rcl);
				}
			}
			return new ParserResult("All candidates were invalid.");
		}
		if (valid.size() == 1) {
			return new ParserResult(valid.get(0).rcl);
		}

		// Rank.
		Candidate best = null;
		boolean duplicate = false;
		for (Candidate candidate : valid) {
			if (best == null || candidate.p > best.p) {
				best = candidate;
			} else if (candidate.p == best.p) {
				duplicate = true;
			}
		}

		// Ranked?
		if (!duplicate && best != null) {
			return new ParserResult(best.rcl);
		}

		// Duplicates?
		if (verbose) {
			for (Candidate candidate : valid) {
				System.out.println("Validated duplicate (P=" + candidate.p
						+ "): " + candidate.rcl);
			}
		}
		return new ParserResult("Validated duplicates.");
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
		for (GssVertex vertex : frontier) {
			if (vertex.parents().size() == 0) {
				Node result = vertex.node();
				if (verbose) {
					if (results.size() == 0) {
						System.out.println();
						System.out.println("RESULTS");
					}
					System.out.println(result);
				}
				results.add(result);
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
		String[] values = tag.equals("relation:") ? new String[] { "above",
				"within" } : new String[] { "reference", "region" };
		Node[] nodes = new Node[values.length];
		for (int i = 0; i < values.length; i++) {
			nodes[i] = new Node(tag, values[i]);
		}
		createFrontier(nodes);
		return true;
	}

	private String matchEllipsis() {

		Node previous = queue.get(-1);
		Node next = queue.get(0);

		if (previous != null) {
			for (EllipsisRule rule : grammar.ellipsisRules()) {
				if (previous.tag.equals(rule.before())) {
					if (rule.after() == null && next == null) {
						return rule.tag();
					}
					if (rule.after() != null && next != null
							&& rule.after().equals(next.tag)) {
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

		// Lexicon mappings.
		String tokens = getTokens(node);
		MappingList mappings = lexicon.getMappings(node.tag, tokens);
		if (mappings == null || mappings.size() == 0) {
			throw new CoreException("Not in lexicon: '" + tokens + "' as "
					+ node);
		}

		// Add.
		Node[] nodes = new Node[mappings.size()];
		for (int i = 0; i < mappings.size(); i++) {
			Node mappedNode = node.clone();
			Mapping mapping = mappings.get(i);
			mappedNode.children.add(0, new Node(mapping.value()));
			mappedNode.p = mapping.p;
			nodes[i] = mappedNode;
		}
		createFrontier(nodes);
	}

	private void createFrontier(Node[] nodes) {

		// Clear.
		GssVertex[] parents = frontier.toArray(new GssVertex[0]);
		frontier.clear();

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
			throw new CoreException("Frontier is empty.");
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
		List<String> rhs = rule.rhs();
		int index = rhs.size() - 1 - step;
		if (index < 0) {
			return;
		}
		GssVertex vertex = path.get(path.size() - 1);
		String tag = rhs.get(index);
		if (!vertex.node().tag.equals(tag)) {
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
		int size = rhs.size();
		Node node = new Node(rule.lhs());
		for (int i = 0; i < size; i++) {
			node.add(path.get(size - 1 - i).node());
		}
		if (node.tag.equals("entity:") && !validateEntity(node)) {
			return;
		}
		GssVertex r = gss.add(node);
		if (verbose) {
			System.out.println();
			System.out.println("REDUCE = " + r);
		}
		GssVertex last = path.get(path.size() - 1);
		for (GssVertex parent : last.parents()) {
			r.parents().add(parent);
		}
		reductionQueue.push(r);
		frontier.add(r);
		if (verbose) {
			printState();
		}
	}

	private boolean validateEntity(Node node) {
		try {
			Entity entity = Entity.fromNode(node);
			if (entity.isType(Type.reference) && entity.relations() != null
					&& entity.relations().size() >= 1) {
				return false;
			}
			if (entity.isType(Type.tile) || entity.isType(Type.reference)
					|| entity.isType(Type.typeReference)
					|| entity.isType(Type.typeReferenceGroup)
					|| entity.isType(Type.region)) {
				return true;
			}
			List<WorldEntity> groundings = planner.getGroundings(entity);
			if (groundings == null || groundings.size() == 0) {
				if (verbose) {
					System.out.println("*** NO GROUNDINGS: " + node);
				}
				return false;
			}
			return true;
		} catch (Exception exception) {
			if (verbose) {
				System.out.println("*** EXCLUDING: " + node);
				exception.printStackTrace(System.out);
			}
			return false;
		}
	}

	private String getTokens(Node node) {
		Node tokenNode = node.getChild("token:");
		if (tokenNode == null) {
			throw new CoreException("Not aligned: " + node);
		}
		int start = Integer.parseInt(tokenNode.children.get(0).tag);
		if (tokenNode.children.size() == 1) {
			return tokens.get(start - 1).getValue();
		}
		int end = Integer.parseInt(tokenNode.children.get(1).tag);
		StringBuilder text = new StringBuilder();
		for (int i = start; i <= end; i++) {
			if (text.length() > 0) {
				text.append('_');
			}
			text.append(tokens.get(i - 1).getValue());
		}
		return text.toString();
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