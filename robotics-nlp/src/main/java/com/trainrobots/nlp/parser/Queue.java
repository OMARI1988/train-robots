/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.parser;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Terminal;

public class Queue {

	private final List<Node> items = new ArrayList<Node>();
	private int position;

	public Queue(Items<Terminal> terminals) {
		for (Terminal terminal : terminals) {
			items.add(new Node(terminal));
		}
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
			throw new RoboticException(
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