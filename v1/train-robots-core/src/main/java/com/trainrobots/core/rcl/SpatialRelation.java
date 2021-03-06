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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.generation.GenerationContext;
import com.trainrobots.core.rcl.generation.Generator;

public class SpatialRelation extends Rcl {

	private final Entity measure;
	private final RelationAttribute relationAttribute;
	private final Entity entity;

	public SpatialRelation(Entity measure, RelationAttribute relationAttribute) {
		this.measure = measure;
		this.relationAttribute = relationAttribute;
		this.entity = null;
	}

	public SpatialRelation(Entity measure, RelationAttribute relationAttribute,
			Entity entity) {
		this.measure = measure;
		this.relationAttribute = relationAttribute;
		this.entity = entity;
	}

	public SpatialRelation(RelationAttribute relationAttribute, Entity entity) {
		this.measure = null;
		this.relationAttribute = relationAttribute;
		this.entity = entity;
	}

	public Entity measure() {
		return measure;
	}

	public RelationAttribute relationAttribute() {
		return relationAttribute;
	}

	public Entity entity() {
		return entity;
	}

	@Override
	public Node toNode() {
		Node node = new Node("spatial-relation:");
		if (measure != null) {
			node.add("measure:", measure.toNode());
		}
		node.add(relationAttribute.toNode());
		if (entity != null) {
			node.add(entity.toNode());
		}
		return node;
	}

	public static SpatialRelation fromNode(Node node) {

		if (!node.hasTag("spatial-relation:")) {
			throw new CoreException("Expected 'spatial-relation:' not '"
					+ node.tag + "'.");
		}

		Entity measure = null;
		RelationAttribute relationAttribute = null;
		Entity entity = null;

		if (node.children != null) {
			for (Node child : node.children) {
				if (child.hasTag("measure:")) {
					measure = Entity.fromNode(child.getSingleChild());
					continue;
				}
				if (child.hasTag("relation:")) {
					relationAttribute = RelationAttribute.fromNode(child);
					continue;
				}
				if (child.hasTag("entity:")) {
					entity = Entity.fromNode(child);
					continue;
				}
				throw new CoreException("Invalid spatial relation tag '"
						+ child.tag + "'.");
			}
		}

		return new SpatialRelation(measure, relationAttribute, entity);
	}

	@Override
	public String generate(GenerationContext context) {
		Generator generator = new Generator(context);
		generator.generate(this);
		return generator.toString();
	}

	@Override
	public void accept(Rcl parent, RclVisitor visitor) {

		// Visit.
		visitor.visit(parent, this);

		// Children.
		if (measure != null) {
			measure.accept(this, visitor);
		}
		if (relationAttribute != null) {
			relationAttribute.accept(this, visitor);
		}
		if (entity != null) {
			entity.accept(this, visitor);
		}
	}
}