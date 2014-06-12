/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.TreeMap;

public enum Relations {

	Above,
	Below,
	Adjacent,
	Left,
	Right,
	Front,
	Forward,
	Back,
	Backward,
	Within,
	Nearest,
	Part;

	private static final TreeMap<String, Relations> relations = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

	public static Relations parse(String name) {
		Relations relation = relations.get(name);
		if (relation == null) {
			throw new IllegalArgumentException(String.format(
					"The relation '%s' is not recognized.", name));
		}
		return relation;
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	static {
		for (Relations relation : values()) {
			relations.put(relation.toString(), relation);
		}
	}
}