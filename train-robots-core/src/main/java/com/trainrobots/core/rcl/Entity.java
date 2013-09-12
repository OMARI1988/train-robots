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

import com.trainrobots.core.rcl.generation.Generator;

public class Entity {

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

	public Iterable<Color> colors() {
		return colors;
	}

	public Iterable<SpatialIndicator> indicators() {
		return indicators;
	}

	public Iterable<SpatialRelation> relations() {
		return relations;
	}

	public String generate() {
		Generator generator = new Generator();
		generator.generate(this);
		return generator.toString();
	}
}