/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.TreeMap;

public enum Colors {

	Blue	(0, 0, 1),
	Cyan	(0, 1, 1),
	Red		(1, 0, 0),
	Yellow	(1, 1, 0),
	Green	(0, 1, 0),
	Magenta	(1, 0, 1),
	White	(1, 1, 1),
	Gray	(0.5f, 0.5f, 0.5f);

	private final float red;
	private final float green;
	private final float blue;

	private Colors(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public float red() {
		return red;
	}

	public float green() {
		return green;
	}

	public float blue() {
		return blue;
	}

	private static final TreeMap<String, Colors> colors = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

	public static Colors parse(String name) {
		Colors color = colors.get(name);
		if (color == null) {
			throw new IllegalArgumentException(String.format(
					"The color '%s' is not recognized.", name));
		}
		return color;
	}

	static {
		for (Colors color : values()) {
			colors.put(color.toString(), color);
		}
	}
}