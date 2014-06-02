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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.configuration.Block;
import com.trainrobots.core.configuration.Configuration;

public class WorldModel {

	private Position arm;
	private boolean gripperOpen;
	private final List<Shape> shapes;
	private final Map<Integer, Shape> map = new HashMap<Integer, Shape>();

	public WorldModel(Position arm, boolean gripperOpen, List<Shape> shapes) {
		this.arm = arm;
		this.gripperOpen = gripperOpen;
		this.shapes = shapes;
		reindex();
	}

	public Position arm() {
		return arm;
	}

	public void reindex() {
		map.clear();
		for (Shape shape : shapes) {
			map.put(getKey(shape.position()), shape);
		}
	}

	public void setArm(Position position) {
		arm = position;
	}

	public void openGripper() {
		gripperOpen = true;
	}

	public void closeGripper() {
		gripperOpen = false;
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

	public Position getDropPosition(int x, int y) {
		for (int z = 0; z <= 7; z++) {
			Position p = new Position(x, y, z);
			if (getShape(p) == null) {
				return p;
			}
		}
		throw new CoreException("Failed to find drop position.");
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

	public WorldModel clone() {
		List<Shape> shapes = new ArrayList<Shape>();
		for (Shape shape : this.shapes) {
			shapes.add(shape.clone());
		}
		return new WorldModel(arm.clone(), gripperOpen, shapes);
	}

	public Configuration toConfiguration() {

		Configuration configuration = new Configuration();

		configuration.armX = arm.x;
		configuration.armY = arm.y;
		configuration.armZ = arm.z;
		configuration.gripperOpen = gripperOpen;
		configuration.blocks = new ArrayList<Block>();

		for (Shape shape : shapes) {
			configuration.blocks.add(shape.toBlock());
		}

		return configuration;
	}
}