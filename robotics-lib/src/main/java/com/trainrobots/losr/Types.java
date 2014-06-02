/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.TreeMap;

public enum Types {

	Cube, Prism;

	private static final TreeMap<String, Types> types = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

	public static Types parse(String name) {
		Types type = types.get(name);
		if (type == null) {
			throw new IllegalArgumentException(String.format(
					"The type '%s' is not recognized.", name));
		}
		return type;
	}

	static {
		for (Types type : values()) {
			types.put(type.toString(), type);
		}
	}
}