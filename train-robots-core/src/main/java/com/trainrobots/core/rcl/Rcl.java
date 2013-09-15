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

public abstract class Rcl {

	public abstract Node toNode();

	public abstract String generate();

	public static Rcl fromString(String text) {
		return fromNode(Node.fromString(text));
	}

	@Override
	public String toString() {
		return toNode().toString();
	}

	public abstract void accept(RclVisitor visitor);

	public Rcl getElement(final int id) {

		class EntityVisitor implements RclVisitor {

			public Entity match;

			@Override
			public void visit(Entity entity) {
				if (entity.id() != null && entity.id().equals(id)) {
					if (match != null) {
						throw new CoreException("Duplicate id " + id);
					}
					match = entity;
				}
			}
		}

		EntityVisitor visitor = new EntityVisitor();
		accept(visitor);
		return visitor.match;
	}

	public String format() {
		return toNode().format();
	}

	public static Rcl fromNode(Node node) {

		if (node.hasTag("entity:")) {
			return Entity.fromNode(node);
		}

		if (node.hasTag("spatial-relation:")) {
			return SpatialRelation.fromNode(node);
		}

		if (node.hasTag("event:")) {
			return Event.fromNode(node);
		}

		if (node.hasTag("sequence:")) {
			return Sequence.fromNode(node);
		}

		throw new CoreException("Unrecognized RCL element '" + node.tag + "'.");
	}
}