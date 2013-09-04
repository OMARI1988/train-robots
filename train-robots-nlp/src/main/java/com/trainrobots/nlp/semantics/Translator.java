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

public class Translator {

	private static final Map<String, String> attributeMap = new TreeMap<String, String>(
			String.CASE_INSENSITIVE_ORDER);

	private Translator() {
	}

	public static Node translate(Node node) {

		// NP
		if (node.hasTag("NP")) {
			return translateNounPhrase(node);
		}

		// NP
		if (node.hasTag("VP")) {
			return translateVerbPhrase(node);
		}

		// PP
		if (node.hasTag("PP")) {
			return translatePrepositionalPhrase(node);
		}

		// Default.
		return getDefaultTranslation(node);
	}

	private static Node getDefaultTranslation(Node node) {

		// Translate children.
		Node copy = new Node(node.tag);
		if (!node.isLeaf()) {
			for (Node child : node.children) {
				copy.add(translate(child));
			}
		}

		// ROOT --> X
		if (copy.hasTag("ROOT") && copy.children.size() == 1) {
			copy = copy.children.get(0);
		}

		// S --> X
		if (copy.hasTag("S") && copy.children.size() == 1) {
			copy = copy.children.get(0);
		}

		// S --> X+ .
		if (copy.hasTag("S")
				&& copy.children.get(copy.children.size() - 1).hasTag(".")) {
			copy.children.remove(copy.children.size() - 1);
		}

		// X --> Y CC Y
		if (copy.children != null && copy.children.size() == 3
				&& copy.children.get(1).hasTag("CC")) {
			copy.tag = "Sequence";
			copy.children.remove(1);
		}

		// Default.
		return copy;
	}

	private static Node translateNounPhrase(Node np) {

		// NP --> NP PP
		if (np.hasChildren("NP", "PP")) {
			Node object = translate(np.children.get(0));
			object.add(translate(np.children.get(1)));
			return object;
		}

		// (NP (PRP it))
		if (np.children.size() == 1) {
			Node child = np.children.get(0);
			if (child.hasTag("PRP")) {
				String pronoun = child.getValue();
				if (pronoun.equals("it")) {
					return new Node("Object", "@X1");
				}
			}
		}

		// Base NP?
		if (!np.allChildrenArePreTerminals()) {
			return getDefaultTranslation(np);
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
			return getDefaultTranslation(np);
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

	private static Node translateVerbPhrase(Node vp) {

		// Command.
		Node command = new Node("Command");
		for (Node child : vp.children) {

			// VB
			if (child.hasTag("VB")) {
				command.add("Action", child.getText().toLowerCase());
			}

			// NP
			else if (child.hasTag("NP")) {
				command.add("Arg", translateNounPhrase(child));
			}

			// PRT
			else if (child.hasTag("PRT") && child.getSingleChild().hasTag("RP")
					&& command.getChild("Action") != null) {
				String particle = child.getSingleChild().getValue();
				command.getChild("Action").getSingleChild().tag += "-"
						+ particle.toLowerCase();
			}

			// Default.
			else {
				command.add(translate(child));
			}
		}
		return command;
	}

	private static Node translatePrepositionalPhrase(Node pp) {

		// PP --> IN + NP
		if (pp.hasChildren("IN", "NP")) {

			// Spatial relation.
			Node spatialRelation = new Node("SpatialRelation");
			spatialRelation.add("Type", pp.getChild("IN").getText());
			spatialRelation.add(translateNounPhrase(pp.getChild("NP")));
			return spatialRelation;
		}

		// No match.
		return getDefaultTranslation(pp);
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
	}
}