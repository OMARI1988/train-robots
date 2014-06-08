/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Color extends Terminal {

	private Colors color;

	public Color(Colors color) {
		super(null);
		this.color = color;
	}

	public Color(TextContext context, Colors color) {
		super(context);
		this.color = color;
	}

	public Colors color() {
		return color;
	}

	public void color(Colors color) {
		this.color = color;
	}

	@Override
	public String name() {
		return "color";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Color) {
			Color color = (Color) losr;
			return color.id == id && color.referenceId == referenceId
					&& color.color == this.color;
		}
		return false;
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(color);
	}
}