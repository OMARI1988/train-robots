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

package com.trainrobots.nlp.processor;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.Color;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.ShapeType;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.PickUpMove;

public class Processor {

	public static List<Move> getMoves(WorldModel world, Rcl rcl) {

		Move move = getMove(world, rcl);
		if (move == null) {
			return null;
		}

		List<Move> moves = new ArrayList<Move>();
		moves.add(move);
		return moves;
	}

	private static Move getMove(WorldModel world, Rcl rcl) {

		// Take.
		Move move = mapTakeCommand(world, rcl);
		if (move != null) {
			return move;
		}

		// Move/above.
		move = mapMoveAboveCommand(world, rcl);
		if (move != null) {
			return move;
		}

		// No match.
		return null;
	}

	private static Move mapTakeCommand(WorldModel world, Rcl rcl) {

		if (!(rcl instanceof Event)) {
			return null;
		}

		Event event = (Event) rcl;
		if (event.action() != Action.take) {
			return null;
		}

		Entity entity = event.entity();
		if (entity == null) {
			return null;
		}

		Position position = mapEntityToPosition(world, entity, false);
		if (position == null) {
			return null;
		}
		return new PickUpMove(position);
	}

	private static Move mapMoveAboveCommand(WorldModel world, Rcl rcl) {

		if (!(rcl instanceof Event)) {
			return null;
		}

		Event event = (Event) rcl;
		if (event.action() != Action.move && event.action() != Action.take
				&& event.action() != Action.drop) {
			return null;
		}

		Entity entity = event.entity();
		if (entity == null) {
			return null;
		}

		Position position = mapEntityToPosition(world, entity, true);
		if (position == null) {
			return null;
		}

		if (event.destinations() == null || event.destinations().size() != 1) {
			return null;
		}
		SpatialRelation destination = event.destinations().get(0);
		Position position2 = mapSpatialRelation(world, destination);
		if (position2 == null) {
			return null;
		}
		return new DirectMove(position, position2);
	}

	private static Position mapSpatialRelation(WorldModel world,
			SpatialRelation relation) {

		SpatialIndicator indicator = relation.indicator();
		if (indicator == null) {
			return null;
		}

		if (indicator != SpatialIndicator.above) {
			return null;
		}

		Entity entity = relation.entity();
		Position position = mapEntityToPosition(world, entity, false);
		if (position == null) {
			return null;

		}
		return position.add(0, 0, 1);
	}

	private static Position mapEntityToPosition(WorldModel world,
			Entity entity, boolean prioritizeGripper) {

		// Color.
		Color color = getEntityColor(entity);

		// Cube.
		Type type = entity.type();
		if (type == Type.cube) {
			return mapShapeToPosition(world, ShapeType.Cube, color,
					prioritizeGripper);
		}

		// Prism.
		if (type == Type.prism) {
			return mapShapeToPosition(world, ShapeType.Prism, color,
					prioritizeGripper);
		}

		// Stack.
		if (type == Type.stack) {
			return mapStackToPosition(world, color);
		}

		// No match.
		return null;
	}

	private static Position mapStackToPosition(WorldModel world, Color color) {

		// Find all stacks.
		List<Stack> stacks = new ArrayList<Stack>();
		for (Shape shape : world.shapes()) {
			if (shape.position.z == 0) {
				Stack stack = null;
				for (int z = 1; z < 8; z++) {
					Shape s2 = world.getShape(shape.position.add(0, 0, z));
					if (s2 != null) {
						if (stack == null) {
							stack = new Stack();
							stack.add(shape);
							stacks.add(stack);
						}
						stack.add(s2);
					}
				}
			}
		}

		// Match.
		Stack result = null;
		for (Stack stack : stacks) {
			if (stack.allHaveColor(color)) {
				if (result != null) {
					return null;
				}
				result = stack;
			}
		}
		return result != null ? result.top().position : null;
	}

	private static Position mapShapeToPosition(WorldModel world,
			ShapeType type, Color color, boolean prioritizeGripper) {

		// Gripper?
		if (prioritizeGripper) {
			Shape shape = world.getShapeInGripper();
			if (shape != null && (color == null || shape.color == color)
					&& shape.type == type) {
				return shape.position;
			}
		}

		// Search the board.
		Shape result = null;
		for (Shape shape : world.shapes()) {
			if ((color == null || shape.color == color) && shape.type == type
					&& world.getShape(shape.position.add(0, 0, 1)) == null) {
				if (result != null) {
					return null;
				}
				result = shape;
			}
		}
		return result != null ? result.position : null;
	}

	private static Color getEntityColor(Entity entity) {
		if (entity.colors() == null || entity.colors().size() != 1) {
			return null;
		}
		return entity.colors().get(0);
	}
}