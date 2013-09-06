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

package com.trainrobots.nlp.parser;

public class Action {

	private final boolean left;
	private final int number;

	private Action(boolean left, int number) {
		this.left = left;
		this.number = number;
	}

	public static Action left(int number) {
		return new Action(true, number);
	}

	public static Action right(int number) {
		return new Action(false, number);
	}

	public boolean left() {
		return left;
	}

	public int number() {
		return number;
	}

	@Override
	public String toString() {
		return (left ? "LEFT " : "RIGHT ") + number;
	}
}