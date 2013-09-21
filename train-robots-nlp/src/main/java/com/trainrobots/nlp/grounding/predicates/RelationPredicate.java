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

package com.trainrobots.nlp.grounding.predicates;

import java.util.List;

import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.nlp.grounding.Grounding;
import com.trainrobots.nlp.scenes.Board;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Edge;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;

public class RelationPredicate implements Predicate {

	private final SpatialIndicator indicator;
	private final List<Grounding> groundings;

	public RelationPredicate(SpatialIndicator indicator,
			List<Grounding> groundings) {
		this.indicator = indicator;
		this.groundings = groundings;
	}

	@Override
	public boolean match(WorldEntity entity) {

		// Match.
		for (Grounding grounding : groundings) {

			// Adjacent.
			if (indicator == SpatialIndicator.adjacent) {

				// Edge.
				if (grounding.entity() instanceof Edge) {
					Edge right = (Edge) grounding.entity();
					if (matchEdge(entity, SpatialIndicator.above, right)) {
						return true;
					}
					continue;
				}

				// Shape.
				Position left = entity.basePosition();
				Position right = grounding.entity().basePosition();
				int dx = left.x - right.x;
				int dy = left.y - right.y;
				if (dx == 0 && (dy == 1 || dy == -1)) {
					return true;
				}
				if (dy == 0 && (dx == 1 || dx == -1)) {
					return true;
				}
				continue;
			}

			// Left.
			if (indicator == SpatialIndicator.left) {
				Position left = entity.basePosition();
				Position right = grounding.entity().basePosition();
				int dx = left.x - right.x;
				int dy = left.y - right.y;
				if (dx == 0 && dy == 1) {
					return true;
				}
				continue;
			}

			// Right.
			if (indicator == SpatialIndicator.right) {
				Position left = entity.basePosition();
				Position right = grounding.entity().basePosition();
				int dx = left.x - right.x;
				int dy = left.y - right.y;
				if (dx == 0 && dy == -1) {
					return true;
				}
				continue;
			}

			// Front.
			if (indicator == SpatialIndicator.front) {
				Position left = entity.basePosition();
				Position right = grounding.entity().basePosition();
				int dx = left.x - right.x;
				int dy = left.y - right.y;
				if (dy == 0 && dx == 1) {
					return true;
				}
				continue;
			}

			// Board.
			if (grounding.entity() instanceof Board) {
				if (indicator == SpatialIndicator.above) {
					Shape left = (Shape) entity;
					if (left.position().z == 0) {
						return true;
					}
				}
				continue;
			}

			// Shape.
			if (grounding.entity() instanceof Shape) {
				if (indicator == SpatialIndicator.above) {
					Shape left = (Shape) entity;
					Shape right = (Shape) grounding.entity();
					if (left.position().equals(right.position().add(0, 0, 1))) {
						return true;
					}
				}
				continue;
			}

			// Corner.
			if (grounding.entity() instanceof Corner) {
				if (indicator == SpatialIndicator.above
						|| indicator == SpatialIndicator.within) {
					Corner right = (Corner) grounding.entity();
					if (entity.basePosition().equals(right.basePosition())) {
						return true;
					}
				}
				continue;
			}

			// Edge.
			if (grounding.entity() instanceof Edge) {
				Edge right = (Edge) grounding.entity();
				if (matchEdge(entity, indicator, right)) {
					return true;
				}
			}

			// Stack.
			if (grounding.entity() instanceof Stack) {
				if (indicator == SpatialIndicator.above) {
					Shape left = (Shape) entity;
					Stack right = (Stack) grounding.entity();
					Shape top = right.getTop();
					if (left.position().equals(top.position().add(0, 0, 1))) {
						return true;
					}
				}
			}
		}

		// No match.
		return false;
	}

	private static boolean matchEdge(WorldEntity entity,
			SpatialIndicator indicator, Edge right) {
		if (indicator == SpatialIndicator.above) {
			if (right == Edge.Right && entity.basePosition().y == 0) {
				return true;
			}
			if (right == Edge.Left && entity.basePosition().y == 7) {
				return true;
			}
			if (right == Edge.Back & entity.basePosition().x == 0) {
				return true;
			}
			if (right == Edge.Front && entity.basePosition().x == 7) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "(spatial-relation: " + indicator + ")";
	}
}