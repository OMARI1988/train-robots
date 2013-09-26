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

package com.trainrobots.nlp.dependency;

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class DependencyConverter {

	private final DependencyGraph graph;

	private DependencyConverter(String text) {

		// Tokens.
		List<Node> nodes = Tokenizer.getTokens(text).children;
		int size = nodes.size();
		String[] tokens = new String[size];
		for (int i = 0; i < size; i++) {
			tokens[i] = nodes.get(i).getValue();
		}
		graph = new DependencyGraph(tokens);
	}

	public static DependencyGraph convert(String text, Rcl rcl) {
		DependencyConverter converter = new DependencyConverter(text);
		converter.convert(rcl);
		return converter.graph;
	}

	private void convert(Rcl rcl) {

		// Sequence.
		if (rcl instanceof Sequence) {
			convert((Sequence) rcl);
			return;
		}

		// Event.
		if (rcl instanceof Event) {
			convert((Event) rcl);
			return;
		}

		// No match.
		throw new CoreException("Unsupported RCL type: '" + rcl.getClass()
				+ "'.");
	}

	private void convert(Sequence sequence) {
		Span head = null;
		for (Event event : sequence.events()) {
			Span span = convert(event);
			if (head == null) {
				head = span;
			} else {
				graph.addEdge(span, head);
				head = span;
			}
		}
	}

	private Span convert(Event event) {
		Span action = getSpan(event.actionAttribute());
		Span entity = convert(event.entity());
		graph.addEdge(entity, action);
		return action;
	}

	private Span convert(Entity entity) {

		// Type.
		Span type = getSpan(entity.typeAttribute());
		if (type == null) {
			type = Span.Ellipsis;
		}

		// Ordinal.
		Span ordinal = getSpan(entity.ordinalAttribute());
		if (ordinal != null) {
			graph.addEdge(ordinal, type);
		}

		// Cardinal.
		Span cardinal = getSpan(entity.cardinalAttribute());
		if (cardinal != null) {
			graph.addEdge(cardinal, type);
		}

		// Colors.
		if (entity.colorAttributes() != null) {
			for (ColorAttribute colorAttribute : entity.colorAttributes()) {
				Span color = getSpan(colorAttribute);
				if (color != null) {
					graph.addEdge(color, type);
				}
			}
		}

		// Indicators.
		if (entity.indicatorAttributes() != null) {
			for (IndicatorAttribute indicatorAttribute : entity
					.indicatorAttributes()) {
				Span indicator = getSpan(indicatorAttribute);
				if (indicator != null) {
					graph.addEdge(indicator, type);
				}
			}
		}

		// TODO: RELATIONS!!
		return type;
	}

	private Span getSpan(Rcl rcl) {
		if (rcl == null || rcl.tokenStart() == 0) {
			return null;
		}
		return new Span(rcl.tokenStart(), rcl.tokenEnd());
	}
}