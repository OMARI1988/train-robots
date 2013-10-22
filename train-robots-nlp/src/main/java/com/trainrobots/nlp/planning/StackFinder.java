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

package com.trainrobots.nlp.planning;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldModel;

public class StackFinder {

	private StackFinder() {
	}

	public static List<Stack> getStacks(WorldModel world) {
		List<Stack> list = new ArrayList<Stack>();
		for (Shape shape : world.shapes()) {
			if (shape.type() == Type.cube && shape.position().z == 0) {
				Stack stack = getStack(world, shape);
				if (stack != null) {
					list.add(stack);
					Stack headlessStack = stack.excludeHead();
					if (headlessStack != null) {
						list.add(headlessStack);
					}
				}
			}
		}
		return list;
	}

	private static Stack getStack(WorldModel world, Shape base) {

		// Look up.
		Stack stack = new Stack(true);
		stack.add(base);
		int x = base.position().x;
		int y = base.position().y;
		for (int z = 1; z <= 8; z++) {

			// Empty?
			Shape shape = world.getShape(new Position(x, y, z));
			if (shape == null) {

				// A stack must be at least two blocks high.
				if (z == 1) {
					return null;
				} else {
					return stack;
				}
			}

			// Add.
			else {
				stack.add(shape);
			}
		}

		// Failed.
		return null;
	}
}