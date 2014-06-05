/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

public class Gripper {

	private final Layout layout;
	private Position position;
	private boolean open;

	public Gripper(Layout layout, Position position, boolean open) {
		this.layout = layout;
		this.position = position;
		this.open = open;
	}

	public Position position() {
		return position;
	}

	public void position(Position position) {
		this.position = position;
		if (layout.listener != null) {
			layout.listener.gripperPositionChanged(position);
		}
	}

	public boolean open() {
		return open;
	}

	public void open(boolean open) {
		this.open = open;
		if (layout.listener != null) {
			layout.listener.gripperOpenChanged(open);
		}
	}

	public Shape shape() {
		return layout.shape(position);
	}
}