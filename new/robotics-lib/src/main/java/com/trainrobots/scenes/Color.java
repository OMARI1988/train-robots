/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import java.util.TreeMap;

public enum Color {

	Blue, Cyan, Red, Yellow, Green, Magenta, Gray, White;

	private static final TreeMap<String, Color> colors = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

	public static Color parse(String name) {
		Color color = colors.get(name);
		if (color == null) {
			throw new IllegalArgumentException(String.format(
					"The color '%s' is not recognized.", name));
		}
		return color;
	}

	static {
		for (Color color : values()) {
			colors.put(color.toString(), color);
		}
	}
}