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
import java.util.List;

public class LexiconEntry {

	private final String token;
	private final List<MappingList> mappingLists = new ArrayList<MappingList>();

	public LexiconEntry(String token) {
		this.token = token;
	}

	public String token() {
		return token;
	}

	public void calculateP() {
		for (MappingList mappings : mappingLists) {
			mappings.calculateP();
		}
	}

	public List<MappingList> getMappings() {
		return mappingLists;
	}

	public MappingList getMappings(String type) {
		for (MappingList mappings : mappingLists) {
			if (mappings.type() == type) {
				return mappings;
			}
		}
		return null;
	}

	public void add(Mapping mapping) {

		MappingList mappings = getMappings(mapping.type());
		if (mappings == null) {
			mappings = new MappingList(mapping.type());
			mappingLists.add(mappings);
		}

		for (Mapping existing : mappings) {
			if (existing.type().equals(mapping.type())
					&& existing.value().equals(mapping.value())) {
				existing.count++;
				return;
			}
		}
		mappings.add(mapping);
		mapping.count = 1;
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append(token);
		text.append(" -->");
		for (MappingList mappings : mappingLists) {
			text.append(' ');
			text.append(mappings);
		}
		return text.toString();
	}
}