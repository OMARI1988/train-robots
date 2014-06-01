/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

public class Shape {

	private final Type type;
	private final Color color;
	private final Position position;

	public Shape(Type type, Color color, Position position) {
		this.type = type;
		this.color = color;
		this.position = position;
	}

	public Type type() {
		return type;
	}

	public Color color() {
		return color;
	}

	public Position position() {
		return position;
	}
}