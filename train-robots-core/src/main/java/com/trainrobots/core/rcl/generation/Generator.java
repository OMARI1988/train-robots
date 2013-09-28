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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.CardinalAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RelationAttribute;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.core.rcl.TypeAttribute;

public class Generator {

	private final GenerationContext context;
	private final StringBuilder text = new StringBuilder();
	private boolean end;

	public Generator(GenerationContext context) {
		this.context = context;
	}

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
		if (entity.isType(Type.reference)) {
			write("it");
			writeAlignment(entity.typeAttribute());
			return;
		}

		// Region?
		if (entity.isType(Type.region) && generateRegion(entity)) {
			return;
		}

		// Determiner.
		if (entity.cardinalAttribute() == null) {
			write("the");
		}

		// Number.
		if (entity.cardinalAttribute() != null) {
			writeCardinal(entity.cardinalAttribute());
		}

		// Indicators.
		if (entity.indicatorAttributes() != null) {
			for (IndicatorAttribute indicatorAttribute : entity
					.indicatorAttributes()) {
				write(indicatorAttribute);
			}
		}

		// Colors.
		if (entity.colorAttributes() != null) {
			writeColors(entity.colorAttributes());
		}

		// Type reference?
		if (entity.isType(Type.typeReference)) {
			write("one");
			writeAlignment(entity.typeAttribute());
		}

		// Type reference group?
		else if (entity.isType(Type.typeReferenceGroup)) {
			write("ones");
			writeAlignment(entity.typeAttribute());
		}

		// Cube group?
		else if (entity.isType(Type.cubeGroup)) {
			write("cubes");
			writeAlignment(entity.typeAttribute());
		}

		// Type.
		else {
			boolean plural = entity.cardinalAttribute() != null
					&& entity.cardinalAttribute().cardinal() > 1;
			write(entity.typeAttribute(), plural);
		}

		// Relation.
		if (entity.relations() != null) {
			for (SpatialRelation relation : entity.relations()) {
				generate(relation);
			}
		}
	}

	public void generate(Event event) {
		write(event.actionAttribute());
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

		RelationAttribute relationAttribute = relation.relationAttribute();
		switch (relationAttribute.relation()) {
		case adjacent:
			write("adjacent to");
			writeAlignment(relationAttribute);
			break;
		case nearest:
			write("nearest to");
			writeAlignment(relationAttribute);
			break;
		case left:
			write(entity ? "left of" : "left");
			writeAlignment(relationAttribute);
			break;
		case right:
			write(entity ? "right of" : "right");
			writeAlignment(relationAttribute);
			break;
		case front:
			write("in front of");
			writeAlignment(relationAttribute);
			break;
		case part:
			write("that is part of");
			writeAlignment(relationAttribute);
			break;
		case forward:
			write(entity ? "in front of" : "forward");
			writeAlignment(relationAttribute);
			break;
		default:
			write(relation.relationAttribute());
			break;
		}

		if (relation.entity() != null) {
			generate(relation.entity());
		}
	}

	private boolean generateRegion(Entity entity) {
		if (entity.indicatorAttributes() == null
				|| entity.indicatorAttributes().size() != 1) {
			return false;
		}
		write("the ");
		write(entity.indicatorAttributes().get(0));
		return true;
	}

	private void write(ActionAttribute actionAttribute) {

		// Action.
		if (actionAttribute.action() == Action.take) {
			write("pick up");
		} else {
			write(actionAttribute.action().toString());
		}

		// Alignment.
		writeAlignment(actionAttribute);
	}

	private void writeColors(List<ColorAttribute> colorAttributes) {

		int size = colorAttributes.size();
		if (size == 0) {
			return;
		}

		int i = 0;
		for (ColorAttribute colorAttribute : colorAttributes) {
			if (i > 0) {
				text.append(i == size - 1 ? " and " : ", ");
			}
			write(colorAttribute);
			i++;
		}
	}

	private void write(ColorAttribute colorAttribute) {

		// Color.
		write(colorAttribute.color().toString());

		// Alignment.
		writeAlignment(colorAttribute);
	}

	private void write(TypeAttribute typeAttribute, boolean plural) {

		// Type.
		if (typeAttribute.type() == Type.tile) {
			write("square");
		} else {
			write(typeAttribute.type().toString());
		}
		if (plural) {
			text.append('s');
		}

		// Alignment.
		writeAlignment(typeAttribute);
	}

	private void write(IndicatorAttribute indicatorAttribute) {

		// Indicator.
		write(indicatorAttribute.indicator().toString());

		// Alignment.
		writeAlignment(indicatorAttribute);
	}

	private void write(RelationAttribute relationAttribute) {

		// Relation.
		write(relationAttribute.relation().toString());

		// Alignment.
		writeAlignment(relationAttribute);
	}

	private void writeCardinal(CardinalAttribute cardinalAttribute) {
		switch (cardinalAttribute.cardinal()) {
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
			write(Integer.toString(cardinalAttribute.cardinal()));
			break;
		}
		writeAlignment(cardinalAttribute);
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

	private void writeAlignment(Rcl rcl) {
		if (context == null || rcl.tokenStart() == 0) {
			return;
		}
		text.append('[');
		boolean first = true;
		for (int i = rcl.tokenStart(); i <= rcl.tokenEnd(); i++) {
			if (first) {
				first = false;
			} else {
				text.append(' ');
			}
			text.append(context.getToken(i));
		}
		text.append(']');
	}
}