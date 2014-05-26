/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

public class Gripper {

	private final Scene scene;
	private Position position;
	private boolean open;

	public Gripper(Scene scene, Position position, boolean open) {
		this.scene = scene;
		this.position = position;
		this.open = open;
	}

	public Position position() {
		return position;
	}

	public void position(Position position) {
		this.position = position;
		if (scene.listener != null) {
			scene.listener.gripperPositionChanged(position);
		}
	}

	public boolean open() {
		return open;
	}

	public void open(boolean open) {
		this.open = open;
		if (scene.listener != null) {
			scene.listener.gripperOpenChanged(open);
		}
	}
}