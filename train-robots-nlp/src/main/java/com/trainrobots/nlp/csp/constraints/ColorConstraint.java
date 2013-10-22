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

package com.trainrobots.nlp.csp.constraints;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Color;
import com.trainrobots.nlp.planning.Model;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;

public class ColorConstraint extends CspConstraint {

	private final Set<Color> colors = new LinkedHashSet<Color>();

	public void add(Color color) {
		colors.add(color);
	}

	public List<WorldEntity> filter(Model model, Iterable<WorldEntity> entities) {
		List<WorldEntity> result = new ArrayList<WorldEntity>();
		for (WorldEntity entity : entities) {

			if ((entity instanceof Shape) && colors.size() == 1) {
				Shape shape = (Shape) entity;
				if (shape.color() == colors.iterator().next()) {
					result.add(entity);
				}
			} else if ((entity instanceof Stack)) {
				Stack stack = (Stack) entity;
				if (stack.hasColors(colors)) {
					result.add(entity);
				}
			}
		}
		return result;
	}

	@Override
	public Node toNode() {
		Node node = new Node("color:");
		for (Color color : colors) {
			node.add(color.toString());
		}
		return node;
	}
}