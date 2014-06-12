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

	Reference,
	TypeReference,
	TypeReferenceGroup,
	Cube,
	CubeGroup,
	Prism,
	Stack,
	Tile,
	Edge,
	Corner,
	Region,
	Board,
	Robot,
	Position;

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

	@Override
	public String toString() {
		switch (this) {
		case TypeReference:
			return "type-reference";
		case TypeReferenceGroup:
			return "type-reference-group";
		case CubeGroup:
			return "cube-group";
		}
		return name().toString().toLowerCase();
	}

	static {

		// Defaults.
		for (Types type : values()) {
			types.put(type.toString(), type);
		}

		// Alternative forms.
		types.put("type-reference", TypeReference);
		types.put("type-reference-group", TypeReferenceGroup);
		types.put("cube-group", CubeGroup);
	}
}