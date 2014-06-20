/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import java.util.HashMap;
import java.util.Map;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;

public class Layout {

	private final int id;
	private final Gripper gripper;
	private final ItemsList<Shape> shapes;
	private final Map<Position, Shape> shapesByPosition = new HashMap<>();
	LayoutListener listener;

	public Layout() {
		this.id = 0;
		this.gripper = new Gripper(this, new Position(3, 3, 4), true);
		this.shapes = new ItemsList();
	}

	public Layout(int id, Position gripperPosition, boolean gripperOpen,
			ItemsList<Shape> shapes) {
		this.id = id;
		this.gripper = new Gripper(this, gripperPosition, gripperOpen);
		this.shapes = shapes;
		for (Shape shape : shapes) {
			shapesByPosition.put(shape.position(), shape);
		}
	}

	public int id() {
		return id;
	}

	public Gripper gripper() {
		return gripper;
	}

	public Shape shape(Position position) {
		return shapesByPosition.get(position);
	}

	public Items<Shape> shapes() {
		return shapes;
	}

	public Layout clone() {
		return new Layout(id, gripper.position(), gripper.open(),
				new ItemsList(shapes));
	}

	public void listener(LayoutListener listener) {
		this.listener = listener;
	}

	public void add(Shape shape) {
		shapes.add(shape);
		shapesByPosition.put(shape.position(), shape);
	}

	public void remove(Shape shape) {
		if (!shapes.remove(shape)) {
			throw new RoboticException("Failed to remove shape.");
		}
		shapesByPosition.remove(shape.position());
	}
}