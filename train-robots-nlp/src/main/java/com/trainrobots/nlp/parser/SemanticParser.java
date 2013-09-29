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
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.parser.grammar.Grammar;
import com.trainrobots.nlp.parser.lexicon.Lexicon;
import com.trainrobots.nlp.processor.Processor;
import com.trainrobots.nlp.scenes.WorldModel;

public class SemanticParser {

	private final Parser parser;
	private final Lexicon lexicon;
	private final List<Node> tokens;
	private final Processor processor;
	private final boolean verbose;

	// private final WorldModel world;

	public SemanticParser(WorldModel world, Grammar grammar, Lexicon lexicon,
			List<Node> items, List<Node> tokens) {
		this(world, grammar, lexicon, items, tokens, false);
	}

	public SemanticParser(WorldModel world, Grammar grammar, Lexicon lexicon,
			List<Node> items, List<Node> tokens, boolean verbose) {
		this.parser = new Parser(grammar, items);
		this.lexicon = lexicon;
		this.tokens = tokens;
		this.processor = new Processor(world);
		this.verbose = verbose;
		// this.world = world;
	}

	public Rcl parse() {

		// Parse.
		List<Node> trees = parser.parse();

		// Map.
		List<Rcl> results = new ArrayList<Rcl>();
		for (Node tree : trees) {
			mapNode(tree);
			Rcl rcl;
			try {
				rcl = Rcl.fromNode(tree);
			} catch (Exception exception) {
				if (verbose) {
					System.out.println("Failed to create RCL: " + tree);
					exception.printStackTrace(System.out);
				}
				continue;
			}
			process(rcl);
			results.add(rcl);
		}

		// Diagnostics.
		for (Rcl rcl : results) {
			if (valid(rcl)) {
				return rcl;
			}
		}
		return null;
	}

	private boolean valid(Rcl rcl) {
		try {
			processor.getMoves(rcl);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	private void process(Rcl rcl) {

		if (rcl instanceof Sequence) {
			Sequence sequence = (Sequence) rcl;
			if (sequence.events().size() == 2) {
				Event event1 = sequence.events().get(0);
				Event event2 = sequence.events().get(1);
				event1.actionAttribute().setAction(Action.take);
				event2.actionAttribute().setAction(Action.drop);

				Entity entity1 = event1.entity();
				Entity entity2 = event2.entity();

				if (entity2.isType(Type.reference)) {
					entity1.setId(1);
					entity2.setReferenceId(1);
				}
			}
		}
	}

	private void mapNode(Node node) {

		// No children?
		if (node.children == null) {

			// Ellipsis?
			if (node.tag.equals("relation:")) {
				node.add("above");
			} else if (node.tag.equals("type:")) {
				node.add("reference");
			}
			return;
		}

		// Alignment?
		int[] tokens = getTokens(node);
		if (tokens != null) {
			mapNode(node, tokens[0], tokens[1]);
			return;
		}

		// Recurse.
		for (Node child : node.children) {
			mapNode(child);
		}
	}

	private void mapNode(Node node, int tokenStart, int tokenEnd) {

		// Build token.
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
		String mapping = lexicon.getMostFrequentMapping(node.tag, token);
		if (mapping == null) {
			if (verbose) {
				System.out.println("Not in lexicon: " + token);
			}
		} else {
			node.children.add(0, new Node(mapping));
		}
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
}