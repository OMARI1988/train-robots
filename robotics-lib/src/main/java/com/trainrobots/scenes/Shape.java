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
import com.trainrobots.observables.Observable;

public class Shape implements Observable {

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

	@Override
	public boolean equals(Object object) {
		if (object instanceof Shape) {
			Shape shape = (Shape) object;
			return shape.type == type && shape.color == color
					&& shape.position.equals(position);
		}
		return false;
	}
}