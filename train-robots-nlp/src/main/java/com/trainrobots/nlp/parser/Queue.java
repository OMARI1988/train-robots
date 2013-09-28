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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;

public class Queue {

	private final List<Node> items;
	private int position;

	public Queue(Node... items) {
		this.items = new ArrayList(Arrays.asList(items));
	}

	public Queue(List<Node> items) {
		this.items = items;
	}

	public boolean empty() {
		return position >= items.size();
	}

	public Node get(int index) {
		index += position;
		return index >= 0 && index < items.size() ? items.get(index) : null;
	}

	public Node read() {
		if (position >= items.size()) {
			throw new CoreException(
					"Failed to read beyond the end of the queue.");
		}
		return items.get(position++);
	}

	@Override
	public String toString() {

		// Empty?
		int size = items.size();
		if (position >= size) {
			return "EMPTY";
		}

		// Format.
		StringBuilder text = new StringBuilder();
		for (int i = position; i < size; i++) {
			if (text.length() > 0) {
				text.append(' ');
			}
			text.append(items.get(i));
		}
		return text.toString();
	}
}