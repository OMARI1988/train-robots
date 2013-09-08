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
import com.trainrobots.nlp.scenes.Move;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.ShapeType;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.trees.Node;

public class Agent {

	public static List<Move> getMoves(WorldModel world, Node node) {

		if (!node.hasTag("Command")) {
			return null;
		}

		String action = node.getValue("Action");
		if (!action.equals("place")) {
			return null;
		}

		Node object = node.getChild("Object");
		if (object == null) {
			return null;
		}

		Shape shape = getShape(world, object);
		if (shape == null) {
			return null;
		}

		Node spatialIndicator = node.getChild("SpatialIndicator");
		if (spatialIndicator == null) {
			return null;
		}
		if (!spatialIndicator.hasLeaf("above")) {
			return null;
		}

		Node object2 = spatialIndicator.getChild("Object");
		Shape shape2 = getShape(world, object2);
		if (shape2 == null) {
			return null;
		}

		Position p1 = shape.position;
		Position p2 = shape2.position.add(0, 0, 1);

		List<Move> moves = new ArrayList<Move>();
		moves.add(new Move(p1, p2));
		return moves;
	}

	public static Shape getShape(WorldModel model, Node node) {
		Color color = getObjectColor(node);
		ShapeType type = getObjectShape(node);
		if (color == null || type == null) {
			return null;
		}
		Shape result = null;
		for (Shape shape : model.shapes()) {
			if (shape.color == color && shape.type == type) {
				if (result != null) {
					return result;
				}
				result = shape;
			}
		}
		return result;
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
				if (attribute.equals("Gray")) {
					return Color.Gray;
				}
				if (attribute.equals("white")) {
					return Color.White;
				}
			}
		}
		return null;
	}

	private static ShapeType getObjectShape(Node node) {
		String value = node.getValue("Type");
		if (value.equals("cube")) {
			return ShapeType.Cube;
		}
		if (value.equals("prism")) {
			return ShapeType.Prism;
		}
		return null;
	}
}