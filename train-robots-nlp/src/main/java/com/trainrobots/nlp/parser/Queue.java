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
import java.util.Arrays;
import java.util.List;

import com.trainrobots.nlp.NlpException;
import com.trainrobots.nlp.trees.Node;

public class Queue {

	private final List<Node> nodes;
	private int position;

	public Queue(Node... nodes) {
		this.nodes = new ArrayList(Arrays.asList(nodes));
	}

	public Queue(List<Node> nodes) {
		this.nodes = nodes;
	}

	public boolean empty() {
		return position >= nodes.size();
	}

	public Node get(int index) {
		index += position;
		return index >= 0 && index < nodes.size() ? nodes.get(index) : null;
	}

	public Node read() {
		if (position >= nodes.size()) {
			throw new NlpException(
					"Failed to read beyond the end of the queue.");
		}
		return nodes.get(position++);
	}

	@Override
	public String toString() {

		// Empty?
		int size = nodes.size();
		if (position >= size) {
			return "EMPTY";
		}

		// Format.
		StringBuilder text = new StringBuilder();
		for (int i = position; i < size; i++) {
			if (text.length() > 0) {
				text.append(' ');
			}
			text.append(nodes.get(i));
		}
		return text.toString();
	}
}