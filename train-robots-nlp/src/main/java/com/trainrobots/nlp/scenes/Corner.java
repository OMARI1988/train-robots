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

import com.trainrobots.core.rcl.Type;

public class Corner implements WorldEntity {

	public static Corner BackRight = new Corner(new Position(0, 0, 0));
	public static Corner BackLeft = new Corner(new Position(0, 7, 0));
	public static Corner FrontRight = new Corner(new Position(7, 0, 0));
	public static Corner FrontLeft = new Corner(new Position(7, 7, 0));

	private final Position basePosition;

	private Corner(Position basePosition) {
		this.basePosition = basePosition;
	}

	@Override
	public String toString() {
		return "corner " + basePosition;
	}

	@Override
	public Type type() {
		return Type.corner;
	}

	@Override
	public Position basePosition() {
		return basePosition;
	}
}