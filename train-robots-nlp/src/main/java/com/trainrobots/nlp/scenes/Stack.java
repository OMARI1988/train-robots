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

package com.trainrobots.nlp.scenes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.trainrobots.core.rcl.Color;
import com.trainrobots.core.rcl.Type;

public class Stack implements WorldEntity {

	private final List<Shape> shapes = new ArrayList<Shape>();
	private final Set<Color> colors = new HashSet<Color>();
	private final boolean includesHead;

	public Stack(boolean includesHead) {
		this.includesHead = includesHead;
	}

	@Override
	public Type type() {
		return Type.stack;
	}

	public boolean includesHead() {
		return includesHead;
	}

	public void add(Shape shape) {
		shapes.add(shape);
		colors.add(shape.color());
	}

	public Shape getTop() {
		return shapes.get(shapes.size() - 1);
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append(includesHead ? "stack " : "headless-stack ");
		text.append(shapes.get(0).position());
		return text.toString();
	}

	@Override
	public Position basePosition() {
		Position p = shapes.get(0).position();
		return new Position(p.x, p.y, 0);
	}

	public boolean hasColors(Set<Color> colors) {
		return this.colors.equals(colors);
	}

	public Stack excludeHead() {

		// Must have at least 3 shapes.
		int size = shapes.size();
		if (size < 2) {
			return null;
		}

		// Create new stack.
		Stack headlessStack = new Stack(false);
		for (int i = 0; i < size - 1; i++) {
			headlessStack.add(shapes.get(i));
		}
		return headlessStack;
	}
}