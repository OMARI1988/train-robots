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

package com.trainrobots.nlp.parser.partial;

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.tagging.Tagger;

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
		Node node = new Node("unknown-sequence:");
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

			switch (action.type()) {
			case Left:
				list.left(action.number());
				break;
			case Right:
				list.right(action.number());
				break;
			case Unary:
				list.unary(action.number(), action.tag());
				break;
			case Remove:
				list.remove(action.number());
				break;
			default:
				throw new CoreException("Unrecognized action: " + action);
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

			// Context.
			Node left = list.get(i - 1);
			Node node = list.get(i);
			Node right = list.get(i + 1);
			Node right2 = list.get(i + 2);

			// attribute: conjunction: attribute:
			if (conjunction(node) && attribute(right)) {
				return Action.left(i);
			}
			if (attribute(node) && conjunction(right) && !right.isPreTerminal()) {
				return Action.left(i);
			}

			// Attach attribute: or ordinal: to entity:
			if ((attribute(node) || ordinal(node) || cardinal(node))
					&& entity(right)) {
				return Action.right(i);
			}

			// Attach entity: or anaphor: to event:
			if (event(node) && (entity(right) || anaphor(right))) {
				return Action.left(i);
			}

			// Attach entity: to spatial-relation:
			if (indicator(node) && entity(right)) {
				return Action.unary(i, "spatial-relation:");
			}
			if (spatialRelation(node) && entity(right)) {
				return Action.left(i);
			}

			// Attach spatial-relation: to event:
			if (event(node) && spatialRelation(right) && right2 == null) {
				return Action.unary(i + 1, "destination:");
			}
			if (event(node) && destination(right) && right2 == null) {
				return Action.left(i);
			}

			// event: conjunction: event:
			if (event(left) && conjunction(node) && event(right)
					&& right2 == null) {
				return Action.remove(i);
			}
			if (event(node) && event(right) && right2 == null) {
				return Action.unary(i, "sequence:");
			}
			if (sequence(node) && event(right) && right2 == null) {
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
		Node node = new Node("unknown:");
		for (Node child : list) {
			node.add(child);
		}
		return node;
	}

	private static boolean sequence(Node node) {
		return node != null && node.tag.equals("sequence:");
	}

	private static boolean event(Node node) {
		return node != null && node.tag.equals("event:");
	}

	private static boolean attribute(Node node) {
		return node != null
				&& (node.tag.equals("attribute:") || node.tag.equals("color:"));
	}

	private static boolean ordinal(Node node) {
		return node != null && node.tag.equals("ordinal:");
	}

	private static boolean cardinal(Node node) {
		return node != null && node.tag.equals("cardinal:");
	}

	private static boolean entity(Node node) {
		return node != null && node.tag.equals("entity:");
	}

	private static boolean indicator(Node node) {
		return node != null && node.tag.equals("indicator:");
	}

	private static boolean spatialRelation(Node node) {
		return node != null && node.tag.equals("spatial-relation:");
	}

	private static boolean destination(Node node) {
		return node != null && node.tag.equals("destination:");
	}

	private static boolean anaphor(Node node) {
		return node != null && node.tag.equals("anaphor:");
	}

	private static boolean conjunction(Node node) {
		return node != null && node.tag.equals("conjunction:");
	}
}