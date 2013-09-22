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

import java.util.Arrays;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.generation.Generator;

public class Event extends Rcl {

	private final Action action;
	private final Entity entity;
	private final List<SpatialRelation> destinations;

	public Event(Action action, Entity entity,
			List<SpatialRelation> destinations) {
		this.action = action;
		this.entity = entity;
		this.destinations = destinations;
	}

	public Event(Action action, Entity entity, SpatialRelation... destinations) {
		this(action, entity, Arrays.asList(destinations));
	}

	public Action action() {
		return action;
	}

	public Entity entity() {
		return entity;
	}

	public List<SpatialRelation> destinations() {
		return destinations;
	}

	@Override
	public Node toNode() {
		Node node = new Node("event:");
		if (action != null) {
			node.add("action:", action.toString().toLowerCase());
		}
		if (entity != null) {
			node.add(entity.toNode());
		}
		if (destinations != null) {
			for (SpatialRelation destination : destinations) {
				node.add("destination:", destination.toNode());
			}
		}
		return node;
	}

	public static Event fromString(String text) {
		return fromNode(Node.fromString(text));
	}

	public static Event fromNode(Node node) {

		if (!node.hasTag("event:")) {
			throw new CoreException("Expected 'event:' not '" + node.tag + "'.");
		}

		Action action = null;
		Entity entity = null;
		SpatialRelation destination = null;

		if (node.children != null) {
			for (Node child : node.children) {
				if (child.hasTag("action:")) {
					action = Action.valueOf(child.getValue());
					continue;
				}
				if (child.hasTag("entity:")) {
					entity = Entity.fromNode(child);
					continue;
				}
				if (child.hasTag("destination:")) {
					destination = SpatialRelation.fromNode(child
							.getSingleChild());
					continue;
				}
				throw new CoreException("Invalid event tag '" + child.tag
						+ "'.");
			}
		}

		if (destination == null) {
			return new Event(action, entity);
		}
		return new Event(action, entity, destination);
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
		if (entity != null) {
			entity.accept(visitor);
		}
		if (destinations != null) {
			for (SpatialRelation destination : destinations) {
				destination.accept(visitor);
			}
		}
	}
}