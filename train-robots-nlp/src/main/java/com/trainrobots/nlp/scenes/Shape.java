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

import com.trainrobots.core.configuration.Block;
import com.trainrobots.core.rcl.Color;
import com.trainrobots.core.rcl.Type;

public class Shape implements WorldEntity {

	private final Color color;
	private final Type type;
	private Position position;

	public Shape(Color color, Type type, Position position) {
		this.color = color;
		this.type = type;
		this.position = position;
	}

	public Color color() {
		return color;
	}

	@Override
	public Type type() {
		return type;
	}

	public Position position() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		Shape s = (Shape) object;
		return s.color == color && s.type == type
				&& s.position.equals(position);
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append(color);
		text.append(' ');
		text.append(type);
		text.append(' ');
		text.append(position);
		return text.toString();
	}

	@Override
	public Position basePosition() {
		return new Position(position.x, position.y, 0);
	}

	public Shape clone() {
		return new Shape(color, type, position.clone());
	}

	public Block toBlock() {
		return new Block(color.code(), type == Type.cube ? Block.CUBE
				: Block.PYRAMID, position.x, position.y, position.z);
	}
}