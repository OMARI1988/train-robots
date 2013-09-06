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

import com.trainrobots.nlp.NlpException;
import com.trainrobots.nlp.trees.Node;

public class Stack {

	private final List<Node> nodes = new ArrayList<Node>();
	private int size;

	public boolean empty() {
		return size == 0;
	}

	public Node get(int index) {
		index = size - index - 1;
		return index >= 0 && index < nodes.size() ? nodes.get(index) : null;
	}

	public void push(Node node) {
		if (size < nodes.size()) {
			nodes.set(size, node);
		} else {
			nodes.add(node);
		}
		size++;
	}

	public void pop() {
		pop(1);
	}

	public void pop(int count) {
		if (count > size) {
			throw new NlpException(
					"Failed to read beyond the end of the stack.");
		}
		size -= count;
	}

	@Override
	public String toString() {

		// Empty?
		if (size == 0) {
			return "EMPTY";
		}

		// Format.
		StringBuilder text = new StringBuilder();
		for (int i = size - 1; i >= 0; i--) {
			if (text.length() > 0) {
				text.append(' ');
			}
			text.append(nodes.get(i));
		}
		return text.toString();
	}
}