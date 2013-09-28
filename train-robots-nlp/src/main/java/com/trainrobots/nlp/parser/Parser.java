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

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclType;
import com.trainrobots.core.rcl.TypeAttribute;

public class Parser {

	private final Stack stack = new Stack();
	private final Queue queue;
	private final boolean verbose;

	public Parser(List<Rcl> items) {
		this(false, items);
	}

	public Parser(boolean verbose, List<Rcl> items) {

		this.verbose = verbose;
		this.queue = new Queue(items);

		if (verbose) {
			System.out.println("START");
			System.out.println("    Q = " + queue);
		}
	}

	public Rcl rcl() {
		if (queue.empty() && stack.size() == 1) {
			return stack.get(0);
		}
		throw new CoreException("Failed to parse a single RCL element.");
	}

	public void shift() {

		Rcl rcl = queue.read();
		stack.push(rcl);

		if (verbose) {
			System.out.println();
			System.out.println("SHIFT");
			System.out.println("    Q = " + queue);
			System.out.println("    S = " + stack);
		}
	}

	public void reduce(int size, RclType type) {

		if (verbose) {
			System.out.println();
			System.out.println("REDUCE " + size + " " + type);
		}

		switch (type) {
		case Entity:
			reduceEntity(size);
			break;
		case Event:
			reduceEvent(size);
			break;
		default:
			throw new CoreException("Invalid RCL type '" + type
					+ "'for reduce operation.");
		}

		if (verbose) {
			System.out.println("    Q = " + queue);
			System.out.println("    S = " + stack);
		}
	}

	private void reduceEntity(int size) {

		// entity --> color type
		if (size == 2) {
			Rcl s0 = stack.pop();
			Rcl s1 = stack.pop();
			if (s1 instanceof ColorAttribute && s0 instanceof TypeAttribute) {
				ColorAttribute color = (ColorAttribute) s1;
				TypeAttribute type = (TypeAttribute) s0;
				stack.push(new Entity(color, type));
				return;
			}
		}

		// No match
		throw new CoreException("Invalid entity reduction.");
	}

	private void reduceEvent(int size) {

		// event --> action entity
		if (size == 2) {
			Rcl s0 = stack.pop();
			Rcl s1 = stack.pop();
			if (s1 instanceof ActionAttribute && s0 instanceof Entity) {
				ActionAttribute action = (ActionAttribute) s1;
				Entity entity = (Entity) s0;
				stack.push(new Event(action, entity));
				return;
			}
		}

		// No match
		throw new CoreException("Invalid event reduction.");
	}
}