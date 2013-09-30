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
import com.trainrobots.nlp.parser.grammar.EllipsisRule;
import com.trainrobots.nlp.parser.grammar.Grammar;
import com.trainrobots.nlp.parser.grammar.ProductionRule;
import com.trainrobots.nlp.parser.lexicon.Lexicon;

public class Parser {

	private final Gss gss = new Gss();
	private final Queue queue;
	private final Grammar grammar;
	private final Lexicon lexicon;
	private final List<Node> tokens;
	private final List<GssNode> frontier = new ArrayList<GssNode>();
	private final LinkedList<GssNode> reductionQueue = new LinkedList<GssNode>();
	private final boolean verbose;

	public Parser(Grammar grammar, Lexicon lexicon, List<Node> items,
			List<Node> tokens) {
		this(grammar, lexicon, items, tokens, false);
	}

	public Parser(Grammar grammar, Lexicon lexicon, List<Node> items,
			List<Node> tokens, boolean verbose) {

		this.grammar = grammar;
		this.lexicon = lexicon;
		this.queue = new Queue(items);
		this.tokens = tokens;
		this.verbose = verbose;

		if (verbose) {
			System.out.println("INITIAL");
			printState();
		}
	}

	public Gss gss() {
		return gss;
	}

	public List<Node> parse() {

		// Parse.
		while (true) {

			if (ellipsis()) {
				reduce();
			}

			if (queue.empty()) {
				break;
			}

			shift();
			reduce();
		}

		// Results.
		List<Node> results = new ArrayList<Node>();
		for (GssNode node : frontier) {
			if (node.parents().size() == 0) {
				Node result = node.content().clone();
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
		Node[] content = new Node[values.length];
		for (int i = 0; i < values.length; i++) {
			content[i] = new Node(tag, values[i]);
		}
		add(content);
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

		// Add.
		Node[] content = getMappedNodes(queue.read());
		if (content == null || content.length == 0) {
			throw new CoreException("DEFAULT_MAPPING_NOT_IMPLEMENTED");
		}
		add(content);
	}

	private void add(Node[] content) {

		// Add node to previous frontier.
		GssNode[] nodes = new GssNode[content.length];
		for (int i = 0; i < content.length; i++) {
			nodes[i] = gss.add(content[i]);
			for (GssNode parent : frontier) {
				nodes[i].parents().add(parent);
			}
		}

		// Create new frontier.
		frontier.clear();
		for (GssNode node : nodes) {
			frontier.add(node);
		}

		// Diagnostics.
		if (verbose) {
			printState();
		}
	}

	private Node[] getMappedNodes(Node node) {

		// Alignment?
		int[] span = getTokens(node);
		if (span == null) {
			return null;
		}

		// Build token.
		int tokenStart = span[0];
		int tokenEnd = span[1];
		StringBuilder text = new StringBuilder();
		for (int i = tokenStart; i <= tokenEnd; i++) {
			if (text.length() > 0) {
				text.append('_');
			}
			text.append(tokens.get(i - 1).getValue());
		}
		String token = text.toString();

		// Already mapped?
		if (node.hasSingleLeaf()) {
			throw new CoreException("Already mapped: " + node);
		}

		// Map.
		List<String> mappings = lexicon.getMappings(node.tag, token);
		if (mappings == null || mappings.size() == 0) {
			if (verbose) {
				System.out.println("Not in lexicon: " + token);
			}
			return null;
		}

		// Result.
		Node[] result = new Node[mappings.size()];
		for (int i = 0; i < mappings.size(); i++) {
			Node mappedNode = node.clone();
			mappedNode.children.add(0, new Node(mappings.get(i)));
			result[i] = mappedNode;
		}
		return result;
	}

	private static int[] getTokens(Node node) {
		Node tokenNode = node.getChild("token:");
		if (tokenNode == null) {
			return null;
		}
		int tokenStart = Integer.parseInt(tokenNode.children.get(0).tag);
		int tokenEnd = tokenStart;
		if (tokenNode.children.size() >= 2) {
			tokenEnd = Integer.parseInt(tokenNode.children.get(1).tag);
		}
		return new int[] { tokenStart, tokenEnd };
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

	private void reduce(GssNode top) {
		List<GssNode> path = new ArrayList<GssNode>();
		for (ProductionRule rule : grammar.productionRules()) {
			path.clear();
			path.add(top);
			if (!match(rule, 0, path)) {
				continue;
			}

			List<String> rhs = rule.rhs();
			int size = rhs.size();
			Node node = new Node(rule.lhs());
			node.children = new ArrayList<Node>();
			for (int i = 0; i < size; i++) {
				Node child = path.get(size - 1 - i).content();
				if (!child.tag.equals(rhs.get(i))) {
					throw new CoreException("Path/rule mismatch.");
				}
				node.children.add(child);
			}

			GssNode reducedNode = gss.add(node);
			if (verbose) {
				System.out.println();
				System.out.println("REDUCE = " + reducedNode);
			}
			GssNode last = path.get(path.size() - 1);
			for (GssNode parent : last.parents()) {
				reducedNode.parents().add(parent);
			}
			reductionQueue.push(reducedNode);
			frontier.add(reducedNode);
			if (verbose) {
				printState();
			}
		}
	}

	private static boolean match(ProductionRule rule, int step,
			List<GssNode> path) {

		List<String> rhs = rule.rhs();
		int index = rhs.size() - 1 - step;
		if (index < 0) {
			return false;
		}

		GssNode node = path.get(path.size() - 1);
		String tag = rhs.get(index);
		if (!node.content().tag.equals(tag)) {
			return false;
		}

		for (GssNode parent : node.parents()) {
			path.add(parent);
			if (match(rule, step + 1, path)) {
				return true;
			}
			path.remove(path.size() - 1);
		}

		return index == 0;
	}

	private void printState() {

		// Queue.
		System.out.println("Q = " + queue);

		// Empty stack?
		int size = gss.nodes().size();
		if (size == 0) {
			System.out.println("S = EMPTY");
			return;
		}

		// Nodes.
		for (GssNode node : gss.nodes()) {
			System.out.print("N" + node.id());
			if (node.parents().size() > 0) {
				System.out.print(" (-->");
				for (GssNode parent : node.parents()) {
					System.out.print(' ');
					System.out.print("N" + parent.id());
				}
				System.out.print(")");
			}
			System.out.println(" = " + node.content());
		}
	}
}