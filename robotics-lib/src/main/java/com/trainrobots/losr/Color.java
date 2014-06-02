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
		super(null);
		this.color = color;
	}

	public Color(TokenContext context, Colors color) {
		super(context);
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
	protected void writeName(StringBuilder text) {
		text.append("color");
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(color.toString().toLowerCase());
	}
}