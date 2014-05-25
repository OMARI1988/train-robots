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

	Blue(java.awt.Color.BLUE), Cyan(java.awt.Color.CYAN), Red(
			java.awt.Color.RED), Yellow(java.awt.Color.YELLOW), Green(
			java.awt.Color.GREEN), Magenta(java.awt.Color.MAGENTA), Gray(
			java.awt.Color.GRAY), White(java.awt.Color.WHITE);

	private final java.awt.Color color;

	private Color(java.awt.Color color) {
		this.color = color;
	}

	public java.awt.Color color() {
		return color;
	}

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