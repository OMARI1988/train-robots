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

package com.trainrobots.nlp.syntax;

import java.util.ArrayList;
import java.util.List;

public class Node {

	public String tag;
	public List<Node> children;

	public Node() {
	}

	public Node(String tag) {
		this.tag = tag;
	}

	public boolean isLeaf() {
		return children == null;
	}

	public boolean isPreTerminal() {
		return children != null && children.size() == 1
				&& children.get(0).isLeaf();
	}

	public Node add(String tag) {
		if (children == null) {
			children = new ArrayList<Node>();
		}
		Node node = new Node(tag);
		children.add(node);
		return node;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toStringBuilder(sb);
		return sb.toString();
	}

	public void toStringBuilder(StringBuilder sb) {
		if (!isLeaf()) {
			sb.append('(');
		}
		if (tag != null) {
			sb.append(tag);
		}
		if (!isLeaf()) {
			for (Node child : children) {
				sb.append(' ');
				child.toStringBuilder(sb);
			}
			sb.append(')');
		}
	}

	public static Node fromString(String text) {
		return new NodeReader(text).read();
	}
}