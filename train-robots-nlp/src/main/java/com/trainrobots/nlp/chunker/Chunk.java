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

package com.trainrobots.nlp.chunker;

public class Chunk {

	private final String tag;
	private final String[] tokens;

	public Chunk(String tag, String[] tokens) {
		this.tag = tag;
		this.tokens = tokens;
	}

	public String tag() {
		return tag;
	}

	public String[] tokens() {
		return tokens;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(tag);
		for (int i = 0; i < tokens.length; i++) {
			sb.append(' ');
			sb.append(tokens[i]);
		}
		return sb.toString();
	}
}