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
import java.util.Iterator;
import java.util.List;

import com.trainrobots.core.nodes.Node;

public class PartialList implements Iterable<Node> {

	private final List<Node> nodes;

	public PartialList(Node... nodes) {
		this.nodes = new ArrayList(Arrays.asList(nodes));
	}

	public PartialList(List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public Iterator<Node> iterator() {
		return nodes.iterator();
	}

	@Override
	public String toString() {

		// Empty?
		int size = nodes.size();
		if (size == 0) {
			return "EMPTY";
		}

		// Format.
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if (text.length() > 0) {
				text.append(' ');
			}
			text.append(nodes.get(i));
		}
		return text.toString();
	}

	public String format() {

		// Empty?
		int size = nodes.size();
		if (size == 0) {
			return "EMPTY";
		}

		// Format.
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if (text.length() > 0) {
				text.append("\r\n");
			}
			text.append("P");
			text.append(i + 1);
			text.append(" = ");
			text.append(nodes.get(i).format());
		}
		return text.toString();
	}

	public int size() {
		return nodes.size();
	}

	public Node get(int number) {
		return number >= 1 && number <= nodes.size() ? nodes.get(number - 1)
				: null;
	}

	public void left(int number) {
		Node head = nodes.get(number - 1);
		Node dependent = nodes.remove(number);
		head.add(dependent);
	}

	public void right(int number) {
		Node head = nodes.get(number);
		Node dependent = nodes.remove(number - 1);
		head.children.add(0, dependent);
	}
}