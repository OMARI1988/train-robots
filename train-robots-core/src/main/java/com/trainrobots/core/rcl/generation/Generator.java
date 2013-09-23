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
import java.util.Set;

import com.trainrobots.core.CoreException;
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
	private boolean end;

	@Override
	public String toString() {
		if (!end) {
			throw new CoreException("Not completed.");
		}
		return text.toString();
	}

	public void end() {
		if (end) {
			throw new CoreException("Already completed.");
		}
		text.append('.');
		end = true;
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

		// Region?
		if (entity.type() == Type.region && generateRegion(entity)) {
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
			writeColors(entity.colors());
		}

		// Type reference?
		if (entity.type() == Type.typeReference) {
			write("one");
		}

		// Type reference group?
		else if (entity.type() == Type.typeReferenceGroup) {
			write("ones");
		}

		// Cube group?
		else if (entity.type() == Type.cubeGroup) {
			write("cubes");
		}

		// Type.
		else {
			boolean plural = entity.cardinal() != null && entity.cardinal() > 1;
			write(entity.type(), plural);
		}

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

		List<SpatialRelation> destinations = event.destinations();
		if (destinations != null) {
			for (int i = 0; i < destinations.size(); i++) {
				SpatialRelation destination = destinations.get(i);
				generate(destination);
			}
		}
	}

	public void generate(SpatialRelation relation) {

		if (relation.measure() != null) {
			generate(relation.measure());
		}

		boolean entity = relation.entity() != null;

		switch (relation.indicator()) {
		case adjacent:
			write("adjacent to");
			break;
		case nearest:
			write("nearest to");
			break;
		case left:
			write(entity ? "left of" : "left");
			break;
		case right:
			write(entity ? "right of" : "right");
			break;
		case front:
			write("in front of");
			break;
		case part:
			write("that is part of");
			break;
		case forward:
			write(entity ? "in front of" : "forward");
			break;
		default:
			write(relation.indicator());
			break;
		}

		if (relation.entity() != null) {
			generate(relation.entity());
		}
	}

	private boolean generateRegion(Entity entity) {
		if (entity.indicators() == null || entity.indicators().size() != 1) {
			return false;
		}
		write("the ");
		write(entity.indicators().get(0));
		return true;
	}

	private void write(Action action) {
		if (action == Action.take) {
			write("pick up");
		} else {
			write(action.toString());
		}
	}

	private void writeColors(Set<Color> colors) {

		int size = colors.size();
		if (size == 0) {
			return;
		}

		int i = 0;
		for (Color color : colors) {
			if (i > 0) {
				text.append(i == size - 1 ? " and " : ", ");
			}
			write(color);
			i++;
		}
	}

	private void write(Color color) {
		write(color.toString());
	}

	private void write(Type type, boolean plural) {
		if (type == Type.tile) {
			write("square");
		} else {
			write(type.toString());
		}
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

		// First word?
		int size = this.text.length();
		if (size == 0) {
			this.text.append(Character.toUpperCase(text.charAt(0)));
			this.text.append(text.substring(1));
			return;
		}

		// Write.
		if (size > 0 && this.text.charAt(size - 1) != ' ') {
			this.text.append(' ');
		}
		this.text.append(text);
	}
}