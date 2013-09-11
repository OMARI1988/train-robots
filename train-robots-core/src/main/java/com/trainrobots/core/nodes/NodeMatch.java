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

package com.trainrobots.core.nodes;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;

public class NodeMatch {

	private final Node search;
	private final Node replace;
	private final List<Node> parameters = new ArrayList<Node>();

	public NodeMatch(String search, String replace) {
		this.search = Node.fromString(search);
		this.replace = Node.fromString(replace);
	}

	public void apply(Node node) {
		apply(null, node);
	}

	private void apply(Node parent, Node node) {

		// Match.
		parameters.clear();
		if (match(node, search)) {
			int index = parent.children.indexOf(node);
			parent.children.set(index, getReplacement(replace));
			return;
		}

		// Recurse.
		if (node.children != null) {
			for (Node child : node.children) {
				apply(node, child);
			}
		}
	}

	private Node getReplacement(Node node) {

		// X
		int number = getParameterNumber(node);
		if (number >= 1) {
			return parameters.get(number - 1).clone();
		}

		// Recurse.
		Node copy = new Node(node.tag);
		if (node.children != null) {
			for (Node child : node.children) {
				copy.add(getReplacement(child));
			}
		}
		return copy;
	}

	private boolean match(Node node, Node search) {
		int number = getParameterNumber(search);
		if (number >= 1) {
			if (parameters.size() != number - 1) {
				throw new CoreException("Parameter number mismatch.");
			}
			parameters.add(node);
			return true;
		}
		if (!match(node.tag, search.tag)) {
			return false;
		}
		if (search.isLeaf()) {
			return node.isLeaf();
		}
		int size = search.children.size();
		if (node.children.size() != size) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (!match(node.children.get(i), search.children.get(i))) {
				return false;
			}
		}
		return true;
	}

	private int getParameterNumber(Node node) {
		if (!node.tag.startsWith("^")) {
			return 0;
		}
		try {
			return Integer.parseInt(node.tag.substring(1));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private static boolean match(String text1, String text2) {
		return text1.equalsIgnoreCase(text2);
	}
}