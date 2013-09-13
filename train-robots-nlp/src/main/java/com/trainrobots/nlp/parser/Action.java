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

	private final ActionType type;
	private final int number;
	private final String tag;

	private Action(ActionType type, int number) {
		this.type = type;
		this.number = number;
		this.tag = null;
	}

	private Action(ActionType type, int number, String tag) {
		this.type = type;
		this.number = number;
		this.tag = tag;
	}

	public static Action left(int number) {
		return new Action(ActionType.Left, number);
	}

	public static Action right(int number) {
		return new Action(ActionType.Right, number);
	}

	public static Action unary(int number, String tag) {
		return new Action(ActionType.Unary, number, tag);
	}

	public ActionType type() {
		return type;
	}

	public int number() {
		return number;
	}

	public String tag() {
		return tag;
	}

	@Override
	public String toString() {
		switch (type) {
		case Left:
			return "LEFT " + number;
		case Right:
			return "RIGHT " + number;
		default:
			return "UNARY " + number + " " + tag;
		}
	}
}