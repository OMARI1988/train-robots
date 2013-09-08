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

package com.trainrobots.core.configuration;

public class Block {

	public static final char BLUE = 'B';
	public static final char CYAN = 'C';
	public static final char RED = 'R';
	public static final char YELLOW = 'Y';
	public static final char GREEN = 'G';
	public static final char MAGENTA = 'M';
	public static final char GRAY = 'X';
	public static final char WHITE = 'W';

	public static final int CUBE = 1;
	public static final int PYRAMID = 2;

	public Block() {
	}

	public Block(char color, int type) {
		this.color = color;
		this.type = type;
	}

	public Block(char color, int type, int x, int y, int z) {
		this.color = color;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public char color;
	public int type;
	public int x;
	public int y;
	public int z;
}