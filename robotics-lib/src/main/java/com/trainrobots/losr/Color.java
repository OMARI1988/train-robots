/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Color extends Terminal {

	private final Colors color;

	public Color(Colors color) {
		this.color = color;
	}

	public Colors color() {
		return color;
	}

	@Override
	public boolean equals(Losr losr) {
		return losr instanceof Color && ((Color) losr).color == color;
	}

	@Override
	public void write(StringBuilder text) {
		text.append("(color: ");
		text.append(color.toString().toLowerCase());
		text.append(')');
	}
}