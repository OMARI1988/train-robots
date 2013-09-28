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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;

public class Parser {

	private final Stack stack = new Stack();
	private final Queue queue;
	private final boolean verbose;

	public Parser(List<Node> items) {
		this(false, items);
	}

	public Parser(boolean verbose, List<Node> items) {

		this.verbose = verbose;
		this.queue = new Queue(items);

		if (verbose) {
			System.out.println("START");
			System.out.println("    Q = " + queue);
		}
	}

	public Node result() {
		if (queue.empty() && stack.size() == 1) {
			return stack.get(0);
		}
		throw new CoreException("Failed to parse a single result.");
	}

	public void shift() {

		Node node = queue.read();
		stack.push(node);

		if (verbose) {
			System.out.println();
			System.out.println("SHIFT");
			System.out.println("    Q = " + queue);
			System.out.println("    S = " + stack);
		}
	}

	public void reduce(int size, String type) {

		if (verbose) {
			System.out.println();
			System.out.println("REDUCE " + size + " " + type);
		}

		Node parent = new Node(type);
		for (int i = 0; i < size; i++) {
			if (parent.children == null) {
				parent.children = new ArrayList<Node>();
			}
			parent.children.add(0, stack.pop());
		}
		stack.push(parent);

		if (verbose) {
			System.out.println("    Q = " + queue);
			System.out.println("    S = " + stack);
		}
	}
}