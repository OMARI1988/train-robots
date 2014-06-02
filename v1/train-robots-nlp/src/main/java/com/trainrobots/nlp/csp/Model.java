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

package com.trainrobots.nlp.csp;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.scenes.Board;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Edge;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Robot;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;

public class Model {

	private final WorldModel world;
	private final List<WorldEntity> entities = new ArrayList<WorldEntity>();

	public Model(WorldModel world) {

		// World.
		this.world = world;

		// Shapes.
		for (Shape shape : world.shapes()) {
			entities.add(shape);
		}

		// Stacks.
		for (Shape shape : world.shapes()) {
			if (shape.type() == Type.cube && shape.position().z == 0) {
				Stack stack = getStack(shape);
				if (stack != null) {
					entities.add(stack);
					Stack headlessStack = stack.excludeHead();
					if (headlessStack != null) {
						entities.add(headlessStack);
					}
				}
			}
		}

		// Corners.
		entities.add(Corner.BackRight);
		entities.add(Corner.BackLeft);
		entities.add(Corner.FrontRight);
		entities.add(Corner.FrontLeft);

		// Edges.
		entities.add(Edge.Front);
		entities.add(Edge.Back);
		entities.add(Edge.Left);
		entities.add(Edge.Right);

		// Board.
		entities.add(Board.TheBoard);

		// Robot.
		entities.add(Robot.TheRobot);
	}

	public WorldModel world() {
		return world;
	}

	public List<WorldEntity> entities() {
		return entities;
	}

	private Stack getStack(Shape base) {

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