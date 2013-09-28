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

package com.trainrobots.nlp.parser.lexicon;

public class Mapping {

	private final String type;
	private final String value;
	public int count;

	public Mapping(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String type() {
		return type;
	}

	public String value() {
		return value;
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append(type);
		text.append('|');
		text.append(value);
		return text.toString();
	}
}