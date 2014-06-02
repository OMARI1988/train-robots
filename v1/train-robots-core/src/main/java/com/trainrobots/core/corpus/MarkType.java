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

package com.trainrobots.core.corpus;

public enum MarkType {

	Unmarked(0), Spam(1), SpellingOrGrammar(2), ImageConfusion(3), BadDirections(
			4), NotSpecific(5), Accurate(6);

	private static final MarkType[] marks = { Unmarked, Spam,
			SpellingOrGrammar, ImageConfusion, BadDirections, NotSpecific,
			Accurate };

	private int value;

	private MarkType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static MarkType getMark(int value) {
		return marks[value];
	}
}