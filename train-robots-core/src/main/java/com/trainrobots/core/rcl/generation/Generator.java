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

package com.trainrobots.core.rcl.generation;

import java.util.List;

import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.Color;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;

public class Generator {

	private final StringBuilder text = new StringBuilder();

	@Override
	public String toString() {
		return text.toString();
	}

	public void generate(Sequence sequence) {
		List<Event> events = sequence.events();
		int size = events.size();
		for (int i = 0; i < size; i++) {
			if (i > 0) {
				write("and");
			}
			generate(events.get(i));
		}
	}

	public void generate(Entity entity) {

		// Reference?
		if (entity.type() == Type.reference) {
			write("it");
			return;
		}

		// Determiner.
		if (entity.cardinal() == null) {
			write("the");
		}

		// Number.
		if (entity.cardinal() != null) {
			writeCardinal(entity.cardinal());
		}

		// Indicators.
		if (entity.indicators() != null) {
			for (SpatialIndicator indicator : entity.indicators()) {
				write(indicator);
			}
		}

		// Colors.
		if (entity.colors() != null) {
			for (Color color : entity.colors()) {
				write(color);
			}
		}

		// Type.
		boolean plural = entity.cardinal() != null && entity.cardinal() > 1;
		write(entity.type(), plural);

		// Relation.
		if (entity.relations() != null) {
			for (SpatialRelation relation : entity.relations()) {
				generate(relation);
			}
		}
	}

	public void generate(Event event) {
		write(event.action());
		generate(event.entity());
		if (event.destinations() != null) {
			for (SpatialRelation destination : event.destinations()) {
				generate(destination);
			}
		}
	}

	public void generate(SpatialRelation relation) {

		if (relation.measure() != null) {
			generate(relation.measure());
		}

		switch (relation.indicator()) {
		case part:
			write("that is part of");
			break;
		default:
			write(relation.indicator());
			break;
		}

		if (relation.entity() != null) {
			generate(relation.entity());
		}
	}

	private void write(Action action) {
		write(action.toString());
	}

	private void write(Color color) {
		write(color.toString());
	}

	private void write(Type type, boolean plural) {
		write(type.toString());
		if (plural) {
			text.append('s');
		}
	}

	private void write(SpatialIndicator indicator) {
		write(indicator.toString());
	}

	private void writeCardinal(int cardinal) {
		switch (cardinal) {
		case 1:
			write("one");
			break;
		case 2:
			write("two");
			break;
		case 3:
			write("three");
			break;
		case 4:
			write("four");
			break;
		case 5:
			write("five");
			break;
		case 6:
			write("six");
			break;
		case 7:
			write("seven");
			break;
		case 8:
			write("eight");
			break;
		case 9:
			write("nine");
			break;
		default:
			write(Integer.toString(cardinal));
			break;
		}
	}

	private void write(String text) {
		int size = this.text.length();
		if (size > 0 && this.text.charAt(size - 1) != ' ') {
			this.text.append(' ');
		}
		this.text.append(text);
	}
}