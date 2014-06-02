/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Types;

public class Shape {

	private final Types type;
	private final Colors color;
	private final Position position;

	public Shape(Types type, Colors color, Position position) {
		this.type = type;
		this.color = color;
		this.position = position;
	}

	public Types type() {
		return type;
	}

	public Colors color() {
		return color;
	}

	public Position position() {
		return position;
	}
}