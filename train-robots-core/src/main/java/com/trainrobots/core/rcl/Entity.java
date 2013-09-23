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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.generation.Generator;

public class Entity extends Rcl {

	private final Integer id;
	private final Integer referenceId;
	private final TypeAttribute typeAttribute;
	private final Integer ordinal;
	private final Integer cardinal;
	private final Set<Color> colors;
	private final List<SpatialIndicator> indicators;
	private final List<SpatialRelation> relations;

	public Entity(TypeAttribute typeAttribute) {
		this.id = null;
		this.referenceId = null;
		this.typeAttribute = typeAttribute;
		this.ordinal = null;
		this.cardinal = null;
		this.colors = null;
		this.indicators = null;
		this.relations = null;
	}

	public Entity(TypeAttribute typeAttribute, int referenceId) {
		this.id = null;
		this.referenceId = referenceId;
		this.typeAttribute = typeAttribute;
		this.ordinal = null;
		this.cardinal = null;
		this.colors = null;
		this.indicators = null;
		this.relations = null;
	}

	public Entity(Color color, TypeAttribute typeAttribute) {
		this.id = null;
		this.referenceId = null;
		this.typeAttribute = typeAttribute;
		this.ordinal = null;
		this.cardinal = null;
		this.colors = new LinkedHashSet<Color>();
		this.colors.add(color);
		this.indicators = null;
		this.relations = null;
	}

	public Entity(int id, SpatialIndicator indicator,
			TypeAttribute typeAttribute, SpatialRelation relation) {
		this.id = id;
		this.referenceId = null;
		this.typeAttribute = typeAttribute;
		this.ordinal = null;
		this.cardinal = null;
		this.colors = null;
		this.indicators = new ArrayList<SpatialIndicator>();
		this.indicators.add(indicator);
		this.relations = new ArrayList<SpatialRelation>();
		this.relations.add(relation);
	}

	public Entity(SpatialIndicator indicator1, SpatialIndicator indicator2,
			TypeAttribute typeAttribute) {
		this.id = null;
		this.referenceId = null;
		this.typeAttribute = typeAttribute;
		this.ordinal = null;
		this.cardinal = null;
		this.colors = null;
		this.indicators = new ArrayList<SpatialIndicator>();
		this.indicators.add(indicator1);
		this.indicators.add(indicator2);
		this.relations = null;
	}

	public Entity(Integer id, Integer referenceId, TypeAttribute typeAttribute,
			Integer ordinal, Integer cardinal, boolean multiple,
			Set<Color> colors, List<SpatialIndicator> indicators,
			List<SpatialRelation> relations) {

		this.id = id;
		this.referenceId = referenceId;
		this.typeAttribute = typeAttribute;
		this.ordinal = ordinal;
		this.cardinal = cardinal;
		this.colors = colors;
		this.indicators = indicators;
		this.relations = relations;
	}

	public static Entity cardinal(int cardinal, TypeAttribute type) {
		return new Entity(null, null, type, null, cardinal, false, null, null,
				null);
	}

	public Integer id() {
		return id;
	}

	public Integer referenceId() {
		return referenceId;
	}

	public TypeAttribute typeAttribute() {
		return typeAttribute;
	}

	public Integer ordinal() {
		return ordinal;
	}

	public Integer cardinal() {
		return cardinal;
	}

	public Set<Color> colors() {
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
		if (typeAttribute != null) {
			node.add(typeAttribute.toNode());
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
		TypeAttribute typeAttribute = null;
		Integer ordinal = null;
		Integer cardinal = null;
		boolean multiple = false;
		Set<Color> colors = null;
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
						colors = new LinkedHashSet<Color>();
					}
					colors.add(color);
					continue;
				}

				if (child.hasTag("type:")) {
					typeAttribute = TypeAttribute.fromNode(child);
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

		return new Entity(id, referenceId, typeAttribute, ordinal, cardinal,
				multiple, colors, indicators, relations);
	}

	@Override
	public String generate() {
		Generator generator = new Generator();
		generator.generate(this);
		generator.end();
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

	public boolean isType(Type type) {
		return typeAttribute != null && typeAttribute.type() == type;
	}
}