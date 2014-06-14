/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.TreeMap;

public enum Indicators {

	Left,
	Leftmost,
	Right,
	Rightmost,
	Front,
	Back,
	Center,
	Highest,
	Lowest,
	Nearest,
	Individual,
	Active;

	private static final TreeMap<String, Indicators> indicators = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

	public static Indicators parse(String name) {
		Indicators indicator = indicators.get(name);
		if (indicator == null) {
			throw new IllegalArgumentException(String.format(
					"The indicator '%s' is not recognized.", name));
		}
		return indicator;
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	static {
		for (Indicators indicator : values()) {
			indicators.put(indicator.toString(), indicator);
		}
	}
}