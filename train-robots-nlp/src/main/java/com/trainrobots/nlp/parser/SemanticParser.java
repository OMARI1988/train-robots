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

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.parser.grammar.Grammar;
import com.trainrobots.nlp.parser.lexicon.Lexicon;

public class SemanticParser {

	private final Parser parser;
	private final Lexicon lexicon;
	private final List<Node> tokens;
	private final boolean verbose;

	public SemanticParser(Grammar grammar, Lexicon lexicon, List<Node> items,
			List<Node> tokens) {
		this(grammar, lexicon, items, tokens, false);
	}

	public SemanticParser(Grammar grammar, Lexicon lexicon, List<Node> items,
			List<Node> tokens, boolean verbose) {
		this.parser = new Parser(grammar, items, verbose);
		this.lexicon = lexicon;
		this.tokens = tokens;
		this.verbose = verbose;
	}

	public List<Node> parse() {

		// Parse.
		List<Node> trees = parser.parse();

		// Map.
		for (Node tree : trees) {
			if (verbose) {
				System.out.println();
				System.out.println("SEMANTICS");
				System.out.println(tree);
			}
			mapNode(tree);
		}
		return trees;
	}

	private void mapNode(Node node) {

		// No children?
		if (node.children == null) {
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
		if (mapping != null) {
			node.children.add(0, new Node(mapping));
		}

		// Diagnostics.
		if (verbose) {
			System.out.println("Mapped: " + node);
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