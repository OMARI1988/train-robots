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

package com.trainrobots.core.rcl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.generation.Generator;

public class Sequence extends Rcl {

	private final List<Event> events = new ArrayList<Event>();

	public Sequence() {
	}

	public Sequence(Event... events) {
		this.events.addAll(Arrays.asList(events));
	}

	public List<Event> events() {
		return events;
	}

	@Override
	public Node toNode() {
		Node node = new Node("sequence:");
		for (Event event : events) {
			node.add(event.toNode());
		}
		return node;
	}

	public static Sequence fromString(String text) {
		return fromNode(Node.fromString(text));
	}

	public static Sequence fromNode(Node node) {

		if (!node.hasTag("sequence:")) {
			throw new CoreException("Expected 'sequence:' not '" + node.tag
					+ "'.");
		}

		Sequence sequence = new Sequence();
		for (Node child : node.children) {
			sequence.events.add(Event.fromNode(child));
		}
		return sequence;
	}

	@Override
	public String generate() {
		Generator generator = new Generator();
		generator.generate(this);
		return generator.toString();
	}

	@Override
	public void accept(RclVisitor visitor) {
		for (Event event : events) {
			event.accept(visitor);
		}
	}
}