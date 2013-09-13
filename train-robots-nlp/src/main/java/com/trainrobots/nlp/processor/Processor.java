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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.Color;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.ShapeType;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.TakeMove;

public class Processor {

	public static List<Move> getMoves(WorldModel world, Rcl rcl) {
		Move move = getMove(world, rcl);
		List<Move> moves = new ArrayList<Move>();
		moves.add(move);
		return moves;
	}

	private static Move getMove(WorldModel world, Rcl rcl) {

		// Event.
		if (!(rcl instanceof Event)) {
			throw new CoreException("Expected an RCL event.");
		}
		Event event = (Event) rcl;

		// Take.
		if (event.action() == Action.take) {
			return mapTakeCommand(world, event);
		}

		// Move.
		if (event.action() == Action.move) {
			return mapMoveCommand(world, event);
		}

		// No match.
		throw new CoreException("Event action '" + event.action()
				+ "' not recognized.");
	}

	private static Move mapTakeCommand(WorldModel world, Event event) {

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		return new TakeMove(mapEntityToPosition(world, entity, false));
	}

	private static Move mapMoveCommand(WorldModel world, Event event) {

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		Position position = mapEntityToPosition(world, entity, true);

		if (event.destinations() == null || event.destinations().size() != 1) {
			throw new CoreException("Single destination not specified.");
		}

		SpatialRelation destination = event.destinations().get(0);
		Position position2 = mapSpatialRelation(world, destination);
		return new DirectMove(position, position2);
	}

	private static Position mapSpatialRelation(WorldModel world,
			SpatialRelation relation) {

		SpatialIndicator indicator = relation.indicator();
		if (indicator == null) {
			throw new CoreException("Indicator not specified.");
		}

		if (indicator != SpatialIndicator.above
				&& indicator != SpatialIndicator.within) {
			throw new CoreException("Invalid indicator: " + indicator);
		}

		Entity entity = relation.entity();
		Position position = mapEntityToPosition(world, entity, false);
		return position.add(0, 0, 1);
	}

	private static Position mapEntityToPosition(WorldModel world,
			Entity entity, boolean prioritizeGripper) {

		// Color.
		Color color = getEntityColor(entity);

		// Cube.
		switch (entity.type()) {
		case cube:
			return mapShapeToPosition(world, ShapeType.Cube, color,
					prioritizeGripper);
		case prism:
			return mapShapeToPosition(world, ShapeType.Prism, color,
					prioritizeGripper);
		case stack:
			return mapStackToPosition(world, color);
		case corner:
			return mapCornerToPosition(world, entity.indicators());
		}

		// No match.
		throw new CoreException("Failed to map entity '" + entity.type()
				+ "' to position.");
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
					throw new CoreException("Failed to find unique shape.");
				}
				result = shape;
			}
		}
		if (result == null) {
			throw new CoreException("Shape not found.");
		}
		return result.position;
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
					throw new CoreException("Failed to find unique stack.");
				}
				result = stack;
			}
		}
		if (result == null) {
			throw new CoreException("Stack not found.");
		}
		return result.top().position;
	}

	private static Position mapCornerToPosition(WorldModel world,
			List<SpatialIndicator> indicators) {

		boolean front = false;
		boolean back = false;
		boolean left = false;
		boolean right = false;
		for (SpatialIndicator indicator : indicators) {
			switch (indicator) {
			case front:
				front = true;
				break;
			case back:
				back = true;
				break;
			case left:
				left = true;
				break;
			case right:
				right = true;
				break;
			}
		}

		if (back && right) {
			return new Position(0, 0, -1);
		}
		if (back && left) {
			return new Position(0, 7, -1);
		}
		if (front && right) {
			return new Position(7, 0, -1);
		}
		if (front && left) {
			return new Position(7, 7, -1);
		}

		throw new CoreException("Failed to identify corner.");
	}

	private static Color getEntityColor(Entity entity) {
		if (entity.colors() == null || entity.colors().size() != 1) {
			return null;
		}
		return entity.colors().get(0);
	}
}