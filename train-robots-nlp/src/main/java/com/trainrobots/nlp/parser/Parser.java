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

import com.trainrobots.nlp.tagging.Tagger;
import com.trainrobots.nlp.trees.Node;

public class Parser {

	private final PartialList list;
	private final boolean verbose;

	private Parser(boolean verbose, Node sentence) {
		this.verbose = verbose;
		list = new PartialList(sentence.children);
	}

	public static Node parse(String text) {
		return parse(false, text);
	}

	public static Node parse(boolean verbose, String text) {
		Node node = new Node("Sentences");
		List<Node> sentences = Tagger.getSentences(text);
		for (Node sentence : sentences) {
			Parser parser = new Parser(verbose, sentence);
			parser.parse();
			Node result = parser.getResult();
			node.add(result);
		}
		return node.hasSingleChild() ? node.getSingleChild() : node;
	}

	private void parse() {

		if (verbose) {
			System.out.println(list.format());
			System.out.println();
		}

		Action action;
		while ((action = next()) != null) {

			if (action.left()) {
				list.left(action.number());
			} else {
				list.right(action.number());
			}

			if (verbose) {
				System.out.println(action);
				System.out.println();
				System.out.println(list.format());
				System.out.println();
			}
		}
	}

	private Action next() {
		int size = list.size();
		for (int i = 1; i <= size; i++) {

			Node node = list.get(i);
			Node right = list.get(i + 1);
			Node right2 = list.get(i + 2);

			if (attribute(node) && object(right)) {
				return Action.right(i);
			}

			if (cardinal(node) && object(right)) {
				return Action.right(i);
			}

			if (description(node) && object(right)) {
				return Action.right(i);
			}

			if (command(node) && object(right)) {
				return Action.left(i);
			}

			if (command(node) && anaphor(right)) {
				return Action.left(i);
			}

			if (spatialIndicator(node) && object(right)) {
				return Action.left(i);
			}

			if (command(node) && spatialIndicator(right) && right2 == null) {
				return Action.left(i);
			}

			if (conjunction(node) && command(right) && right2 == null) {
				return Action.left(i);
			}

			if (command(node) && conjunction(right) && right2 == null) {
				return Action.left(i);
			}
		}
		return null;
	}

	private Node getResult() {

		// Single node?
		if (list.size() == 1) {
			return list.get(1);
		}

		// Multiple nodes.
		Node node = new Node("Sentence");
		for (Node child : list) {
			node.add(child);
		}
		return node;
	}

	private static boolean command(Node node) {
		return node != null && node.tag.equals("Command");
	}

	private static boolean description(Node node) {
		return node != null && node.tag.equals("Description");
	}

	private static boolean attribute(Node node) {
		return node != null && node.tag.equals("Attribute");
	}

	private static boolean cardinal(Node node) {
		return node != null && node.tag.equals("Cardinal");
	}

	private static boolean object(Node node) {
		return node != null && node.tag.equals("Object");
	}

	private static boolean spatialIndicator(Node node) {
		return node != null && node.tag.equals("SpatialIndicator");
	}

	private static boolean anaphor(Node node) {
		return node != null && node.tag.equals("Anaphor");
	}

	private static boolean conjunction(Node node) {
		return node != null && node.tag.equals("Conjunction");
	}
}