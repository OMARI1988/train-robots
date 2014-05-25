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

public class Scene {

	private final int id;
	private final Gripper gripper;
	private final Items<Shape> shapes;

	public Scene(int id, Gripper gripper, Items<Shape> shapes) {
		this.id = id;
		this.gripper = gripper;
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
}