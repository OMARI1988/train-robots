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

package com.trainrobots.nlp.agent;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.nlp.scenes.Color;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.ShapeType;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.PickUpMove;
import com.trainrobots.nlp.trees.Node;

public class Agent {

	public static List<Move> getMoves(WorldModel world, Node node) {

		Move move = getMove(world, node);
		if (move == null) {
			return null;
		}

		List<Move> moves = new ArrayList<Move>();
		moves.add(move);
		return moves;
	}

	private static Move getMove(WorldModel world, Node node) {

		// Pick-up.
		Move move = mapPickUpCommand(world, node);
		if (move != null) {
			return move;
		}

		// Move/above.
		move = mapMoveAboveCommand(world, node);
		if (move != null) {
			return move;
		}

		// No match.
		return null;
	}

	private static Move mapPickUpCommand(WorldModel world, Node node) {

		if (!node.hasTag("Command")) {
			return null;
		}

		String action = node.getValue("Action");
		if (!action.equals("pick-up") && !action.equals("take")
				&& !action.equals("grab")) {
			return null;
		}

		Node object = node.getChild("Object");
		if (object == null) {
			return null;
		}

		Position position = mapObjectToPosition(world, object, false);
		if (position == null) {
			return null;
		}
		return new PickUpMove(position);
	}

	private static Move mapMoveAboveCommand(WorldModel world, Node node) {

		if (!node.hasTag("Command")) {
			return null;
		}

		String action = node.getValue("Action");
		if (!action.equals("move") && !action.equals("place")
				&& !action.equals("put") && !action.equals("drop")) {
			return null;
		}

		Node object = node.getChild("Object");
		if (object == null) {
			return null;
		}

		Position position = mapObjectToPosition(world, object, true);
		if (position == null) {
			return null;
		}

		Node spatialIndicator = node.getChild("SpatialIndicator");
		if (spatialIndicator == null) {
			return null;
		}
		Position position2 = mapSpatialIndicator(world, spatialIndicator);
		if (position2 == null) {
			return null;
		}
		return new DirectMove(position, position2);
	}

	private static Position mapSpatialIndicator(WorldModel world, Node node) {
		if (!node.hasLeaf("above")) {
			return null;
		}
		Node object = node.getChild("Object");
		Position position = mapObjectToPosition(world, object, false);
		if (position == null) {
			return null;
		}
		return position.add(0, 0, 1);
	}

	private static Position mapObjectToPosition(WorldModel world, Node node,
			boolean prioritizeGripper) {

		// Color.
		Color color = getObjectColor(node);

		// Cube.
		String value = node.getValue("Type");
		if (value.equals("cube")) {
			return mapShapeToPosition(world, ShapeType.Cube, color,
					prioritizeGripper);
		}

		// Prism.
		if (value.equals("prism")) {
			return mapShapeToPosition(world, ShapeType.Prism, color,
					prioritizeGripper);
		}

		// Stack.
		if (value.equals("stack")) {
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

	private static Color getObjectColor(Node node) {
		for (Node child : node.children) {
			if (child.hasTag("Attribute")) {
				String attribute = child.getValue();
				if (attribute.equals("blue")) {
					return Color.Blue;
				}
				if (attribute.equals("cyan")) {
					return Color.Cyan;
				}
				if (attribute.equals("red")) {
					return Color.Red;
				}
				if (attribute.equals("yellow")) {
					return Color.Yellow;
				}
				if (attribute.equals("green")) {
					return Color.Green;
				}
				if (attribute.equals("magenta")) {
					return Color.Magenta;
				}
				if (attribute.equals("gray")) {
					return Color.Gray;
				}
				if (attribute.equals("white")) {
					return Color.White;
				}
			}
		}
		return null;
	}
}