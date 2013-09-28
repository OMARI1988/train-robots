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

import java.util.ArrayList;
import java.util.List;

public class GrammarRule {

	private final String lhs;
	private final List<String> rhs = new ArrayList<String>();
	public int count;

	public GrammarRule(String lhs) {
		this.lhs = lhs;
	}

	public String lhs() {
		return lhs;
	}

	public void add(String rhs) {
		this.rhs.add(rhs);
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append(lhs);
		text.append(" -->");
		for (String item : rhs) {
			text.append(' ');
			text.append(item);
		}
		return text.toString();
	}
}