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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MappingList implements Iterable<Mapping> {

	private final String type;
	private final List<Mapping> mappings = new ArrayList<Mapping>();

	public MappingList(String type) {
		this.type = type;
	}

	public String type() {
		return type;
	}

	@Override
	public Iterator<Mapping> iterator() {
		return mappings.iterator();
	}

	public void add(Mapping mapping) {
		mappings.add(mapping);
	}

	public int size() {
		return mappings.size();
	}

	public Mapping get(int index) {
		return mappings.get(index);
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append('(');
		text.append(type);
		for (Mapping mapping : mappings) {
			text.append(' ');
			text.append(mapping.value());
			text.append('=');
			text.append(mapping.p);
		}
		text.append(")");
		return text.toString();
	}

	public void calculateP() {
		double sum = 0;
		for (Mapping mapping : mappings) {
			sum += mapping.count;
		}
		for (Mapping mapping : mappings) {
			mapping.p = mapping.count / sum;
		}
	}
}