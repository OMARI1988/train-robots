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

	Blue(0, 0, 255),
	Cyan(0, 255, 255),
	Red(255, 0, 0),
	Yellow(255, 255, 0),
	Green(0, 255, 0),
	Magenta(255, 0, 255),
	Gray(128, 128, 128),
	White(255, 255, 255);

	private final int red;
	private final int green;
	private final int blue;

	private Colors(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int red() {
		return red;
	}

	public int green() {
		return green;
	}

	public int blue() {
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