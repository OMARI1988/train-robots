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
import com.trainrobots.core.rcl.generation.GenerationContext;
import com.trainrobots.core.rcl.generation.Generator;

public class Entity extends Rcl {

	private Integer id;
	private Integer referenceId;
	private final TypeAttribute typeAttribute;
	private final OrdinalAttribute ordinalAttribute;
	private final CardinalAttribute cardinalAttribute;
	private final List<ColorAttribute> colorAttributes;
	private final List<IndicatorAttribute> indicatorAttributes;
	private final List<SpatialRelation> relations;

	public Entity(TypeAttribute typeAttribute) {
		this.id = null;
		this.referenceId = null;
		this.typeAttribute = typeAttribute;
		this.ordinalAttribute = null;
		this.cardinalAttribute = null;
		this.colorAttributes = null;
		this.indicatorAttributes = null;
		this.relations = null;
	}

	public Entity(TypeAttribute typeAttribute, int referenceId) {
		this.id = null;
		this.referenceId = referenceId;
		this.typeAttribute = typeAttribute;
		this.ordinalAttribute = null;
		this.cardinalAttribute = null;
		this.colorAttributes = null;
		this.indicatorAttributes = null;
		this.relations = null;
	}

	public Entity(ColorAttribute colorAttribute, TypeAttribute typeAttribute) {
		this.id = null;
		this.referenceId = null;
		this.typeAttribute = typeAttribute;
		this.ordinalAttribute = null;
		this.cardinalAttribute = null;
		this.colorAttributes = new ArrayList<ColorAttribute>();
		this.colorAttributes.add(colorAttribute);
		this.indicatorAttributes = null;
		this.relations = null;
	}

	public Entity(int id, IndicatorAttribute indicatorAttribute,
			TypeAttribute typeAttribute, SpatialRelation relation) {
		this.id = id;
		this.referenceId = null;
		this.typeAttribute = typeAttribute;
		this.ordinalAttribute = null;
		this.cardinalAttribute = null;
		this.colorAttributes = null;
		this.indicatorAttributes = new ArrayList<IndicatorAttribute>();
		this.indicatorAttributes.add(indicatorAttribute);
		this.relations = new ArrayList<SpatialRelation>();
		this.relations.add(relation);
	}

	public Entity(IndicatorAttribute indicatorAttribute1,
			IndicatorAttribute indicatorAttribute2, TypeAttribute typeAttribute) {
		this.id = null;
		this.referenceId = null;
		this.typeAttribute = typeAttribute;
		this.ordinalAttribute = null;
		this.cardinalAttribute = null;
		this.colorAttributes = null;
		this.indicatorAttributes = new ArrayList<IndicatorAttribute>();
		this.indicatorAttributes.add(indicatorAttribute1);
		this.indicatorAttributes.add(indicatorAttribute2);
		this.relations = null;
	}

	public Entity(Integer id, Integer referenceId, TypeAttribute typeAttribute,
			OrdinalAttribute ordinalAttribute,
			CardinalAttribute cardinalAttribute,
			List<ColorAttribute> colorAttributes,
			List<IndicatorAttribute> indicatorAttributes,
			List<SpatialRelation> relations) {

		this.id = id;
		this.referenceId = referenceId;
		this.typeAttribute = typeAttribute;
		this.ordinalAttribute = ordinalAttribute;
		this.cardinalAttribute = cardinalAttribute;
		this.colorAttributes = colorAttributes;
		this.indicatorAttributes = indicatorAttributes;
		this.relations = relations;
	}

	public static Entity cardinal(CardinalAttribute cardinalAttribute,
			TypeAttribute typeAttribute) {
		return new Entity(null, null, typeAttribute, null, cardinalAttribute,
				null, null, null);
	}

	public Integer id() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer referenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public TypeAttribute typeAttribute() {
		return typeAttribute;
	}

	public OrdinalAttribute ordinalAttribute() {
		return ordinalAttribute;
	}

	public CardinalAttribute cardinalAttribute() {
		return cardinalAttribute;
	}

	public List<ColorAttribute> colorAttributes() {
		return colorAttributes;
	}

	public List<IndicatorAttribute> indicatorAttributes() {
		return indicatorAttributes;
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
		if (ordinalAttribute != null) {
			node.add(ordinalAttribute.toNode());
		}

		// Cardinal.
		if (cardinalAttribute != null) {
			node.add(cardinalAttribute.toNode());
		}

		// Indicators.
		if (indicatorAttributes != null) {
			for (IndicatorAttribute indicatorAttribute : indicatorAttributes) {
				node.add(indicatorAttribute.toNode());
			}
		}

		// Colors.
		if (colorAttributes != null) {
			for (ColorAttribute colorAttribute : colorAttributes) {
				node.add(colorAttribute.toNode());
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
		OrdinalAttribute ordinalAttribute = null;
		CardinalAttribute cardinalAttribute = null;
		List<ColorAttribute> colorAttributes = null;
		List<IndicatorAttribute> indicatorAttributes = null;
		List<SpatialRelation> relations = null;

		if (node.children != null) {
			for (Node child : node.children) {

				if (child.hasTag("id:")) {
					id = Integer.parseInt(child.getValue());
					continue;
				}

				if (child.hasTag("indicator:")) {
					IndicatorAttribute indicatorAttribute = IndicatorAttribute
							.fromNode(child);
					if (indicatorAttributes == null) {
						indicatorAttributes = new ArrayList<IndicatorAttribute>();
					}
					indicatorAttributes.add(indicatorAttribute);
					continue;
				}

				if (child.hasTag("ordinal:")) {
					ordinalAttribute = OrdinalAttribute.fromNode(child);
					continue;
				}

				if (child.hasTag("cardinal:")) {
					cardinalAttribute = CardinalAttribute.fromNode(child);
					continue;
				}

				if (child.hasTag("color:")) {
					if (colorAttributes == null) {
						colorAttributes = new ArrayList<ColorAttribute>();
					}
					colorAttributes.add(ColorAttribute.fromNode(child));
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

		return new Entity(id, referenceId, typeAttribute, ordinalAttribute,
				cardinalAttribute, colorAttributes, indicatorAttributes,
				relations);
	}

	@Override
	public String generate(GenerationContext context) {
		Generator generator = new Generator(context);
		generator.generate(this);
		generator.end();
		return generator.toString();
	}

	@Override
	public void accept(Rcl parent, RclVisitor visitor) {

		// Visit.
		visitor.visit(parent, this);

		// Ordinal.
		if (ordinalAttribute != null) {
			visitor.visit(this, ordinalAttribute);
		}

		// Cardinal.
		if (cardinalAttribute != null) {
			visitor.visit(this, cardinalAttribute);
		}

		// Indicators.
		if (indicatorAttributes != null) {
			for (IndicatorAttribute indicatorAttribute : indicatorAttributes) {
				indicatorAttribute.accept(this, visitor);
			}
		}

		// Colors.
		if (colorAttributes != null) {
			for (ColorAttribute colorAttribute : colorAttributes) {
				colorAttribute.accept(this, visitor);
			}
		}

		// Type.
		if (typeAttribute != null) {
			typeAttribute.accept(this, visitor);
		}

		// Relations.
		if (relations != null) {
			for (SpatialRelation relation : relations) {
				relation.accept(this, visitor);
			}
		}
	}

	public boolean isType(Type type) {
		return typeAttribute != null && typeAttribute.type() == type;
	}
}