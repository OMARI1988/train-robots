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

package com.trainrobots.nlp.parser.grammar;

public class EllipsisRule {

	private final String before;
	private final String tag;
	private final String after;
	public int count;

	public EllipsisRule(String before, String tag, String after) {
		this.before = before;
		this.tag = tag;
		this.after = after;
	}

	public String before() {
		return before;
	}

	public String tag() {
		return tag;
	}

	public String after() {
		return after;
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		if (before != null) {
			text.append(before);
		}
		text.append('|');
		text.append(tag);
		text.append('|');
		if (after != null) {
			text.append(after);
		}
		return text.toString();
	}
}