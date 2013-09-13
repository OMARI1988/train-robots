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

package com.trainrobots.core.rcl;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.generation.Generator;

public class Entity extends Rcl {

	private final Integer id;
	private final Integer referenceId;
	private final Type type;
	private final Integer ordinal;
	private final Integer cardinal;
	private final boolean multiple;
	private final List<Color> colors;
	private final List<SpatialIndicator> indicators;
	private final List<SpatialRelation> relations;

	public Entity(Type type) {
		this.id = null;
		this.referenceId = null;
		this.type = type;
		this.ordinal = null;
		this.cardinal = null;
		this.multiple = false;
		this.colors = null;
		this.indicators = null;
		this.relations = null;
	}

	public Entity(Color color, Type type) {
		this.id = null;
		this.referenceId = null;
		this.type = type;
		this.ordinal = null;
		this.cardinal = null;
		this.multiple = false;
		this.colors = new ArrayList<Color>();
		this.colors.add(color);
		this.indicators = null;
		this.relations = null;
	}

	public Entity(SpatialIndicator indicator1, SpatialIndicator indicator2,
			Type type) {
		this.id = null;
		this.referenceId = null;
		this.type = type;
		this.ordinal = null;
		this.cardinal = null;
		this.multiple = false;
		this.colors = null;
		this.indicators = new ArrayList<SpatialIndicator>();
		this.indicators.add(indicator1);
		this.indicators.add(indicator2);
		this.relations = null;
	}

	public Entity(Integer id, Integer referenceId, Type type, Integer ordinal,
			Integer cardinal, boolean multiple, List<Color> colors,
			List<SpatialIndicator> indicators, List<SpatialRelation> relations) {

		this.id = id;
		this.referenceId = referenceId;
		this.type = type;
		this.ordinal = ordinal;
		this.cardinal = cardinal;
		this.multiple = multiple;
		this.colors = colors;
		this.indicators = indicators;
		this.relations = relations;
	}

	public Integer id() {
		return id;
	}

	public Integer referenceId() {
		return referenceId;
	}

	public Type type() {
		return type;
	}

	public Integer ordinal() {
		return ordinal;
	}

	public Integer cardinal() {
		return cardinal;
	}

	public boolean multiple() {
		return multiple;
	}

	public List<Color> colors() {
		return colors;
	}

	public List<SpatialIndicator> indicators() {
		return indicators;
	}

	public List<SpatialRelation> relations() {
		return relations;
	}

	public static Entity fromString(String text) {
		return (Entity) Rcl.fromString(text);
	}

	@Override
	public Node toNode() {

		// Node.
		Node node = new Node("entity:");

		// TODO: FIX!!
		// id;
		// referenceId;
		// ordinal;
		// cardinal;
		// multiple;

		// Indicators.
		if (indicators != null) {
			for (SpatialIndicator indicator : indicators) {
				node.add("spatial-indicator:", indicator.toString()
						.toLowerCase());
			}
		}

		// Colors.
		if (colors != null) {
			for (Color color : colors) {
				node.add("color:", color.toString().toLowerCase());
			}
		}

		// Type.
		if (type != null) {
			node.add("type:", type.toString().toLowerCase());
		}

		// TODO: FIX!!
		// relations;

		// Result.
		return node;
	}

	@Override
	public String generate() {
		Generator generator = new Generator();
		generator.generate(this);
		return generator.toString();
	}
}