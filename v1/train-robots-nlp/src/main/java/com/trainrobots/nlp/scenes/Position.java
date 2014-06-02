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

package com.trainrobots.nlp.scenes;

public class Position {

	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public final int x;
	public final int y;
	public final int z;

	public Position add(int dx, int dy, int dz) {
		return new Position(x + dx, y + dy, z + dz);
	}

	@Override
	public boolean equals(Object object) {
		Position p = (Position) object;
		return p.x == x && p.y == y && p.z == z;
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append('(');
		text.append(x);
		text.append(", ");
		text.append(y);
		text.append(", ");
		text.append(z);
		text.append(')');
		return text.toString();
	}

	public Position clone() {
		return new Position(x, y, z);
	}
}