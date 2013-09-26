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

import com.trainrobots.core.corpus.Command;

public class Expression {

	private final Command command;
	private final String tag;
	private final String[] tokens;
	private final String key;

	public Expression(Command command, String tag, String[] tokens) {
		this.command = command;
		this.tag = tag;
		this.tokens = tokens;
		this.key = buildKey(tokens);
	}

	public Command command() {
		return command;
	}

	public String tag() {
		return tag;
	}

	public String[] tokens() {
		return tokens;
	}

	public String key() {
		return key;
	}

	@Override
	public String toString() {
		return key;
	}

	public static String buildKey(String[] tokens) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			if (i > 0) {
				sb.append('|');
			}
			sb.append(tokens[i]);
		}
		return sb.toString();
	}
}