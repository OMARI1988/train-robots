/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.collections.ItemsList;

public class Layout {

	private final int id;
	private final Gripper gripper;
	private final Items<Shape> shapes;
	LayoutListener listener;

	public Layout() {
		this.id = 0;
		this.gripper = new Gripper(this, new Position(3, 3, 4), true);
		this.shapes = new ItemsList();
	}

	public Layout(int id, Position gripperPosition, boolean gripperOpen,
			Items<Shape> shapes) {
		this.id = id;
		this.gripper = new Gripper(this, gripperPosition, gripperOpen);
		this.shapes = new ItemsArray(shapes.toArray());
	}

	public int id() {
		return id;
	}

	public Gripper gripper() {
		return gripper;
	}

	public Items<Shape> shapes() {
		return shapes;
	}

	public void listener(LayoutListener listener) {
		this.listener = listener;
	}
}