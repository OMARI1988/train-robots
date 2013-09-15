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

import com.trainrobots.core.CoreException;
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

	public Entity(Type type, int referenceId) {
		this.id = null;
		this.referenceId = referenceId;
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

	public Entity(int id, SpatialIndicator indicator, Type type,
			SpatialRelation relation) {
		this.id = id;
		this.referenceId = null;
		this.type = type;
		this.ordinal = null;
		this.cardinal = null;
		this.multiple = false;
		this.colors = null;
		this.indicators = new ArrayList<SpatialIndicator>();
		this.indicators.add(indicator);
		this.relations = new ArrayList<SpatialRelation>();
		this.relations.add(relation);
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

	public static Entity cardinal(int cardinal, Type type) {
		return new Entity(null, null, type, null, cardinal, false, null, null,
				null);
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

		// ID.
		if (id != null) {
			node.add("id:", id.toString());
		}

		// Ordinal.
		if (ordinal != null) {
			node.add("ordinal:", ordinal.toString());
		}

		// Cardinal.
		if (cardinal != null) {
			node.add("cardinal:", cardinal.toString());
		}

		// TODO: FIX!!
		// multiple;

		// Indicators.
		if (indicators != null) {
			for (SpatialIndicator indicator : indicators) {
				node.add("spatial-indicator:", indicator.toString());
			}
		}

		// Colors.
		if (colors != null) {
			for (Color color : colors) {
				node.add("color:", color.toString());
			}
		}

		// Type.
		if (type != null) {
			node.add("type:", type.toString());
		}

		// Reference.
		if (referenceId != null) {
			node.add("reference-id:", referenceId.toString());
		}

		// Relations.
		if (relations != null) {
			for (SpatialRelation relation : relations) {
				node.add(relation.toNode());
			}
		}

		// Result.
		return node;
	}

	public static Entity fromNode(Node node) {

		if (!node.hasTag("entity:")) {
			throw new CoreException("Expected 'entity:' not '" + node.tag
					+ "'.");
		}

		Integer id = null;
		Integer referenceId = null;
		Type type = null;
		Integer ordinal = null;
		Integer cardinal = null;
		boolean multiple = false;
		List<Color> colors = null;
		List<SpatialIndicator> indicators = null;
		List<SpatialRelation> relations = null;

		if (node.children != null) {
			for (Node child : node.children) {

				if (child.hasTag("id:")) {
					id = Integer.parseInt(child.getValue());
					continue;
				}

				if (child.hasTag("spatial-indicator:")) {
					SpatialIndicator indicator = SpatialIndicator.valueOf(child
							.getValue());
					if (indicators == null) {
						indicators = new ArrayList<SpatialIndicator>();
					}
					indicators.add(indicator);
					continue;
				}

				if (child.hasTag("cardinal:")) {
					cardinal = Integer.parseInt(child.getValue());
					continue;
				}

				if (child.hasTag("color:")) {
					Color color = Color.valueOf(child.getValue());
					if (colors == null) {
						colors = new ArrayList<Color>();
					}
					colors.add(color);
					continue;
				}

				if (child.hasTag("type:")) {
					type = Type.parse(child.getValue());
					continue;
				}

				if (child.hasTag("reference-id:")) {
					referenceId = Integer.parseInt(child.getValue());
					continue;
				}

				if (child.hasTag("spatial-relation:")) {
					SpatialRelation relation = SpatialRelation.fromNode(child);
					if (relations == null) {
						relations = new ArrayList<SpatialRelation>();
					}
					relations.add(relation);
					continue;
				}

				throw new CoreException("Invalid entity tag '" + child.tag
						+ "'.");
			}
		}

		return new Entity(id, referenceId, type, ordinal, cardinal, multiple,
				colors, indicators, relations);
	}

	@Override
	public String generate() {
		Generator generator = new Generator();
		generator.generate(this);
		return generator.toString();
	}

	@Override
	public void accept(RclVisitor visitor) {
		visitor.visit(this);
		if (relations != null) {
			for (SpatialRelation relation : relations) {
				relation.accept(visitor);
			}
		}
	}
}