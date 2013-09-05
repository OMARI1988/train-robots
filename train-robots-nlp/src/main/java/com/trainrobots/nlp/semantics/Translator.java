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

package com.trainrobots.nlp.semantics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.trainrobots.nlp.trees.Node;
import com.trainrobots.nlp.trees.NodeMatch;

public class Translator {

	private static final Map<String, String> attributeMap = new TreeMap<String, String>(
			String.CASE_INSENSITIVE_ORDER);

	private static final List<NodeMatch> matches = new ArrayList<NodeMatch>();

	private Translator() {
	}

	public static Node translate(Node node) {

		// Copy.
		node = node.clone();

		// Match.
		for (NodeMatch match : matches) {
			match.apply(node);
		}

		// Translate.
		translate(null, node);

		// Root.
		if (match(node.tag, "ROOT") && node.hasSingleChild()) {
			return node.getSingleChild();
		}

		// Node.
		return node;
	}

	private static void translate(Node parent, Node node) {

		// Children first.
		if (node.children != null) {
			for (Node child : node.children) {
				translate(node, child);
			}
		}

		// S
		if (node.hasSingleChild() && match(node.getSingleChild().tag, "S")) {
			List<Node> list = node.getSingleChild().children;
			node.children = new ArrayList<Node>();
			for (Node child : list) {
				if (!match(child.tag, ".")) {
					node.children.add(child);
				}
			}
			return;
		}

		// X CC X --> Sequence
		if (node.children != null && node.children.size() == 3
				&& match(node.children.get(1).tag, "CC")) {
			node.tag = "Sequence";
			node.children.remove(1);
			return;
		}

		// PP --> SpatialRelation
		if (match(node.tag, "PP")) {
			node.tag = "SpatialRelation";
			if (match(node.children.get(0).tag, "IN")
					|| match(node.children.get(0).tag, "TO")) {
				node.children.get(0).tag = "Type";
			}
			return;
		}

		// VP --> Command
		if (match(node.tag, "VP")) {
			node.tag = "Command";

			String action = null;
			for (int i = 0; i < node.children.size(); i++) {
				Node c = node.children.get(i);
				if (match(c.tag, "VB")) {
					action = c.getValue().toLowerCase();
					node.children.remove(i);
					i--;
					continue;
				}
				if (action != null && match(c.tag, "PRT")) {
					Node child = c.getSingleChild();
					if (match(child.tag, "RP")) {
						action += "-" + child.getValue().toLowerCase();
					}
					node.children.remove(i);
					i--;
					continue;
				}
			}
			if (action == null) {
				action = "unknown";
			}
			node.children.add(0, new Node("Action", action));
			return;
		}

		// NP
		if (match(node.tag, "NP")) {
			Node replacement = translateBaseNounPhrase(node);
			if (replacement != null) {
				int index = parent.children.indexOf(node);
				parent.children.set(index, replacement);
				return;
			}
		}
	}

	private static Node translateBaseNounPhrase(Node np) {

		// (NP (PRP it)) --> (Object @X1)
		if (np.hasSingleChild()) {
			Node child = np.children.get(0);
			if (match(child.tag, "PRP")) {
				String pronoun = child.getValue();
				if (pronoun.equals("it")) {
					return new Node("Object", "@X1");
				}
			}
		}

		// Base NP?
		if (!np.allChildrenArePreTerminals()) {
			return null;
		}

		int size = np.children.size();
		List<String> attributes = new ArrayList<String>();
		String state = null;
		String objectClass = null;

		for (int i = 0; i < size; i++) {
			Node child = np.children.get(i);
			if (child.hasTag("DT") && child.getValue().equals("the")) {
				state = "definite";
			} else if (i == size - 1) {
				objectClass = child.getValue().toLowerCase();
			} else {
				attributes.add(child.getValue().toLowerCase());
			}
		}

		// Class.
		if (objectClass == null) {
			return null;
		}
		Node object = new Node("Object");
		object.add("Class", objectClass);

		// State.
		if (state != null) {
			object.add("State", state);
		}

		// Attributes.
		for (String attribute : attributes) {
			object.add(getAttributeName(attribute), attribute);
		}

		// Result.
		return object;
	}

	private static String getAttributeName(String attribute) {

		// Mapped?
		String name = attributeMap.get(attribute);
		if (name != null) {
			return name;
		}

		// Default.
		return "Attribute";
	}

	static {

		matches.add(new NodeMatch("(VP (VBG sitting) ^1)", "(^1)"));
		matches.add(new NodeMatch("(VP (VBN placed) ^1)", "(^1)"));
		matches.add(new NodeMatch("(VP (VBN located) ^1)", "(^1)"));

		matches.add(new NodeMatch(
				"(PP (IN on) (NP (NP (NN top)) (PP (IN of) ^1)))",
				"(PP (IN above) ^1)"));

		matches.add(new NodeMatch(
				"(PP (IN on) (NP (NP (DT the) (NN top)) (PP (IN of) ^1)))",
				"(PP (IN above) ^1)"));

		matches.add(new NodeMatch(
				"(PP (TO to) (NP (NP (DT the) (NN right)) (PP (IN of) ^1)))",
				"(PP (IN right-of) ^1)"));

		attributeMap.put("red", "Color");
		attributeMap.put("yellow", "Color");
		attributeMap.put("green", "Color");
		attributeMap.put("white", "Color");
		attributeMap.put("pink", "Color");
		attributeMap.put("gray", "Color");
		attributeMap.put("grey", "Color");
		attributeMap.put("purple", "Color");
		attributeMap.put("turquoise", "Color");
		attributeMap.put("blue", "Color");
		attributeMap.put("sky", "Color");
		attributeMap.put("cyan", "Color");

		attributeMap.put("top", "Direction");
		attributeMap.put("left", "Direction");
		attributeMap.put("bottom", "Direction");
		attributeMap.put("right", "Direction");
	}

	private static boolean match(String text1, String text2) {
		return text1.equalsIgnoreCase(text2);
	}
}