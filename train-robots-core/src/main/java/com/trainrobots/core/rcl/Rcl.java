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

public abstract class Rcl {

	public abstract Node toNode();

	public abstract String generate();

	public static Rcl fromNode(Node node) {

		if (node.hasTag("entity:")) {
			return entityFromNode(node);
		}

		if (node.hasTag("spatial-relation:")) {
			return spatialRelationFromNode(node);
		}

		if (node.hasTag("event:")) {
			return eventFromNode(node);
		}

		throw new CoreException("Unrecognized RCL element '" + node.tag + "'.");
	}

	private static Entity entityFromNode(Node node) {

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
				if (child.hasTag("spatial-indicator:")) {
					SpatialIndicator indicator = SpatialIndicator.valueOf(child
							.getValue());
					if (indicators == null) {
						indicators = new ArrayList<SpatialIndicator>();
					}
					indicators.add(indicator);
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
					type = Type.valueOf(child.getValue());
					continue;
				}
				throw new CoreException("Invalid entity tag '" + child.tag
						+ "'.");
			}
		}

		return new Entity(id, referenceId, type, ordinal, cardinal, multiple,
				colors, indicators, relations);
	}

	private static SpatialRelation spatialRelationFromNode(Node node) {

		SpatialIndicator indicator = null;
		Entity entity = null;

		if (node.children != null) {
			for (Node child : node.children) {
				if (child.hasTag("spatial-indicator:")) {
					indicator = SpatialIndicator.valueOf(child.getValue());
					continue;
				}
				if (child.hasTag("entity:")) {
					entity = entityFromNode(child);
					continue;
				}
				throw new CoreException("Invalid spatial relation tag '"
						+ child.tag + "'.");
			}
		}

		return new SpatialRelation(indicator, entity);
	}

	private static Event eventFromNode(Node node) {

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
					entity = entityFromNode(child);
					continue;
				}
				if (child.hasTag("destination:")) {
					destination = spatialRelationFromNode(child
							.getSingleChild());
					continue;
				}
				throw new CoreException("Invalid event tag '" + child.tag
						+ "'.");
			}
		}

		return new Event(action, entity, destination);
	}
}