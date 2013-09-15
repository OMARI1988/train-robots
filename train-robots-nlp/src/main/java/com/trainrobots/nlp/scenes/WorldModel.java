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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldModel {

	private final Position arm;
	private final boolean gripperOpen;
	private final List<Shape> shapes;
	private final Map<Integer, Shape> map = new HashMap<Integer, Shape>();

	public WorldModel(Position arm, boolean gripperOpen, List<Shape> shapes) {

		this.arm = arm;
		this.gripperOpen = gripperOpen;
		this.shapes = shapes;

		for (Shape shape : shapes) {
			map.put(getKey(shape.position()), shape);
		}
	}

	public Shape getShape(Position position) {
		if (position.x >= 0 && position.x <= 7 && position.y >= 0
				&& position.y <= 7 && position.z >= 0 && position.z <= 7) {
			return map.get(getKey(position));
		}
		return null;
	}

	public Shape getShapeInGripper() {
		return getShape(arm);
	}

	public Iterable<Shape> shapes() {
		return shapes;
	}

	@Override
	public String toString() {

		// Arm.
		StringBuilder text = new StringBuilder();
		text.append(gripperOpen ? "Open " : "Closed ");
		text.append(arm);

		// Shapes.
		for (Shape shape : shapes) {
			text.append("\r\n");
			text.append(shape);
		}
		return text.toString();
	}

	private int getKey(Position position) {
		return position.x * 8 * 8 + position.y * 8 + position.z;
	}
}