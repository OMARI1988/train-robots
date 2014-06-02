/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.core.rcl;

public enum Type {
	reference, typeReference, typeReferenceGroup, cube, cubeGroup, prism, corner, board, stack, row, column, edge, tile, robot, region;

	@Override
	public String toString() {
		if (this == typeReference) {
			return "type-reference";
		}
		if (this == typeReferenceGroup) {
			return "type-reference-group";
		}
		if (this == cubeGroup) {
			return "cube-group";
		}
		return name();
	}

	public static Type parse(String name) {
		if (name.equals("type-reference")) {
			return typeReference;
		}
		if (name.equals("type-reference-group")) {
			return typeReferenceGroup;
		}
		if (name.equals("cube-group")) {
			return cubeGroup;
		}
		return Type.valueOf(name);
	}
}