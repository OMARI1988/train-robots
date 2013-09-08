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

package com.trainrobots.nlp.trees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.trainrobots.core.CoreException;

public class Node {

	public String tag;
	public List<Node> children;

	public Node() {
	}

	public Node(String tag) {
		this.tag = tag;
	}

	public Node(String tag, String childTag) {
		this(tag, new Node(childTag));
	}

	public Node(String tag, Node... children) {
		this.tag = tag;
		this.children = new ArrayList<Node>(Arrays.asList(children));
	}

	public boolean isLeaf() {
		return children == null;
	}

	public boolean isPreTerminal() {
		return children != null && children.size() == 1
				&& children.get(0).isLeaf();
	}

	public boolean isChain() {
		return isLeaf()
				|| (children != null && children.size() == 1 && children.get(0)
						.isChain());
	}

	public boolean allChildrenArePreTerminals() {
		if (isLeaf()) {
			return false;
		}
		for (Node child : children) {
			if (!child.isPreTerminal()) {
				return false;
			}
		}
		return true;
	}

	public String getValue() {
		if (!isPreTerminal()) {
			throw new CoreException("Node is not a preterminal: " + this);
		}
		return children.get(0).tag;
	}

	public Node getSingleChild() {
		if (!hasSingleChild()) {
			throw new CoreException("Node does not have a single child: "
					+ this);
		}
		return children.get(0);
	}

	public boolean hasSingleChild() {
		return children != null && children.size() == 1;
	}

	public Node add(String tag) {
		Node node = new Node(tag);
		add(node);
		return node;
	}

	public Node add(String tag, String childTag) {
		Node node = new Node(tag, childTag);
		add(node);
		return node;
	}

	public Node add(String tag, Node child) {
		Node node = new Node(tag, child);
		add(node);
		return node;
	}

	public void add(Node node) {
		if (children == null) {
			children = new ArrayList<Node>();
		}
		children.add(node);
	}

	public Node getChild(String tag) {
		if (children == null) {
			return null;
		}
		Node result = null;
		for (Node child : children) {
			if (child.tag != null && child.hasTag(tag)) {
				if (result != null) {
					throw new CoreException("Duplicate child tag '" + tag
							+ "' in " + this);
				}
				result = child;
			}
		}
		return result;
	}

	public boolean hasTag(String tag) {
		return this.tag.equals(tag);
	}

	public boolean hasChildren(String... tags) {
		if (children.size() != tags.length) {
			return false;
		}
		for (int i = 0; i < tags.length; i++) {
			if (!children.get(i).hasTag(tags[i])) {
				return false;
			}
		}
		return true;
	}

	public Node clone() {
		Node copy = new Node(tag);
		if (children != null) {
			for (Node child : children) {
				copy.add(child.clone());
			}
		}
		return copy;
	}

	public static Node fromString(String text) {
		return new NodeReader(text).read();
	}

	public static List<Node> listFromString(String text) {
		return new NodeReader(text).readList();
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		write(text);
		return text.toString();
	}

	@Override
	public boolean equals(Object object) {

		// Compare tags.
		Node node = (Node) object;
		if (!node.tag.equals(tag)) {
			return false;
		}

		// Children not expected.
		if (children == null) {
			return node.children == null;
		}

		// Children expected.
		int size = children.size();
		if (node.children == null || node.children.size() != size) {
			return false;
		}

		// Match children.
		for (int i = 0; i < size; i++) {
			if (!children.get(i).equals(node.children.get(i))) {
				return false;
			}
		}
		return true;
	}

	public String format() {
		StringBuilder text = new StringBuilder();
		format(text, 0);
		return text.toString();
	}

	public String getText() {
		StringBuilder text = new StringBuilder();
		getText(text);
		return text.toString();
	}

	private void write(StringBuilder text) {
		if (!isLeaf()) {
			text.append('(');
		}
		if (tag != null) {
			text.append(tag);
		}
		if (!isLeaf()) {
			for (Node child : children) {
				text.append(' ');
				child.write(text);
			}
			text.append(')');
		}
	}

	private void format(StringBuilder text, int level) {

		// Leaf.
		if (isLeaf()) {
			for (int i = 0; i < level * 2; i++) {
				text.append(' ');
			}
			text.append(tag);
			return;
		}

		// Non-leaf.
		for (int i = 0; i < level * 2; i++) {
			text.append(' ');
		}
		if (isChain()) {
			write(text);
		} else {
			text.append('(');
			text.append(tag);
			for (int i = 0; i < children.size(); i++) {
				Node child = children.get(i);
				if (i == 0 && child.isLeaf()) {
					text.append(' ');
					text.append(child.tag);
				} else {
					text.append("\r\n");
					child.format(text, level + 1);
				}
			}
			text.append(')');
		}
	}

	private void getText(StringBuilder text) {
		if (isLeaf() && tag != null) {
			if (text.length() > 0) {
				text.append(' ');
			}
			text.append(tag);
		}
		if (!isLeaf()) {
			for (Node child : children) {
				child.getText(text);
			}
		}
	}
}