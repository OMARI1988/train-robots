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

import com.trainrobots.nlp.NlpException;

public class NodeReader {

	private final String text;
	private int position;

	public NodeReader(String text) {
		this.text = text;
	}

	public Node read() {
		if (peek() == '(') {
			return readNonLeaf();
		}
		return new Node(readTag());
	}

	private Node readNonLeaf() {

		// (
		char ch = next();
		if (ch != '(') {
			throw new NlpException("Expected open bracket.");
		}

		// Tag.
		Node node = new Node(readTag());

		// Children.
		while (peek() != ')') {

			// ' '
			ch = next();
			if (ch != ' ') {
				throw new NlpException("Expected space.");
			}

			// Node.
			Node child = read();
			if (node.children == null) {
				node.children = new ArrayList<Node>();
			}
			node.children.add(child);
		}

		// ')'
		ch = next();
		if (ch != ')') {
			throw new NlpException("Expected closing bracket.");
		}

		return node;
	}

	private String readTag() {
		int index = position;
		while (peek() != ' ' && peek() != ')') {
			next();
		}
		String tag = text.substring(index, position);
		return tag;
	}

	private char peek() {
		return text.charAt(position);
	}

	private char next() {
		return text.charAt(position++);
	}
}
