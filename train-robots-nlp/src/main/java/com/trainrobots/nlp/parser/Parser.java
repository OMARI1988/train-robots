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

public class Parser {

	private final Gss gss = new Gss();
	private final Queue queue;
	private final Grammar grammar;
	private final List<GssNode> frontier = new ArrayList<GssNode>();
	private final LinkedList<GssNode> reductionQueue = new LinkedList<GssNode>();
	private final boolean verbose;

	public Parser(Grammar grammar, List<Node> items) {
		this(grammar, items, false);
	}

	public Parser(Grammar grammar, List<Node> items, boolean verbose) {

		this.grammar = grammar;
		this.queue = new Queue(items);
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

		// Add node to previous frontier.
		GssNode node = gss.add(new Node(tag));
		for (GssNode parent : frontier) {
			node.parents().add(parent);
		}

		// Create new frontier.
		frontier.clear();
		frontier.add(node);

		// Diagnostics.
		if (verbose) {
			printState();
		}
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

		// Add node to previous frontier.
		GssNode node = gss.add(queue.read());
		for (GssNode parent : frontier) {
			node.parents().add(parent);
		}

		// Create new frontier.
		frontier.clear();
		frontier.add(node);

		// Diagnostics.
		if (verbose) {
			printState();
		}
	}

	private void reduce() {
		if (frontier.size() != 1) {
			throw new CoreException("Expected single node in frontier.");
		}
		reductionQueue.clear();
		reductionQueue.add(frontier.get(0));
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