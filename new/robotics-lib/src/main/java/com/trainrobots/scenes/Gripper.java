/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

public class Gripper {

	private final Position position;
	private final boolean open;

	public Gripper(Position position, boolean open) {
		this.position = position;
		this.open = open;
	}

	public Position position() {
		return position;
	}

	public boolean open() {
		return open;
	}
}