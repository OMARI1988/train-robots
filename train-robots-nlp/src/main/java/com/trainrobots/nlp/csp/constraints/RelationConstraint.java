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

package com.trainrobots.nlp.csp.constraints;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Relation;
import com.trainrobots.nlp.csp.CspVariable;
import com.trainrobots.nlp.planning.Model;
import com.trainrobots.nlp.scenes.Board;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Edge;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;

public class RelationConstraint extends CspConstraint {

	private final Relation relation;
	private final CspVariable variable;

	public RelationConstraint(Relation relation, CspVariable variable) {
		this.relation = relation;
		this.variable = variable;
	}

	public Relation relation() {
		return relation;
	}

	public CspVariable variable() {
		return variable;
	}

	@Override
	public List<WorldEntity> filter(Model model, Iterable<WorldEntity> entities) {
		List<WorldEntity> groundings = variable.solve(model);
		List<WorldEntity> result = new ArrayList<WorldEntity>();
		for (WorldEntity entity : entities) {
			if (match(entity, groundings)) {
				result.add(entity);
			}
		}
		return result;
	}

	@Override
	public Node toNode() {
		Node node = new Node("relation:", relation.toString());
		node.add(variable.toNode());
		return node;
	}

	private boolean match(WorldEntity entity, List<WorldEntity> groundings) {

		// Match.
		for (WorldEntity grounding : groundings) {

			// Adjacent.
			if (relation == Relation.adjacent) {

				// Edge.
				if (grounding instanceof Edge) {
					Edge right = (Edge) grounding;
					if (matchEdge(entity, Relation.above, right)) {
						return true;
					}
					continue;
				}

				// Shape.
				Position left = entity.basePosition();
				Position right = grounding.basePosition();
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
			if (relation == Relation.left) {
				Position left = entity.basePosition();
				Position right = grounding.basePosition();
				int dx = left.x - right.x;
				int dy = left.y - right.y;
				if (dx == 0 && dy == 1) {
					return true;
				}
				continue;
			}

			// Right.
			if (relation == Relation.right) {
				Position left = entity.basePosition();
				Position right = grounding.basePosition();
				int dx = left.x - right.x;
				int dy = left.y - right.y;
				if (dx == 0 && dy == -1) {
					return true;
				}
				continue;
			}

			// Front.
			if (relation == Relation.front) {
				Position left = entity.basePosition();
				Position right = grounding.basePosition();
				int dx = left.x - right.x;
				int dy = left.y - right.y;
				if (dy == 0 && dx == 1) {
					return true;
				}
				continue;
			}

			// Board.
			if (grounding instanceof Board) {
				if (relation == Relation.above) {
					Shape left = (Shape) entity;
					if (left.position().z == 0) {
						return true;
					}
				}
				continue;
			}

			// Shape.
			if (grounding instanceof Shape) {
				if (relation == Relation.above) {
					Shape left = (Shape) entity;
					Shape right = (Shape) grounding;
					if (left.position().equals(right.position().add(0, 0, 1))) {
						return true;
					}
				}
				continue;
			}

			// Corner.
			if (grounding instanceof Corner) {
				if (relation == Relation.above || relation == Relation.within) {
					Corner right = (Corner) grounding;
					if (entity.basePosition().equals(right.basePosition())) {
						return true;
					}
				}
				continue;
			}

			// Edge.
			if (grounding instanceof Edge) {
				Edge right = (Edge) grounding;
				if (matchEdge(entity, relation, right)) {
					return true;
				}
			}

			// Stack.
			if (grounding instanceof Stack) {
				if (relation == Relation.above) {
					Shape left = (Shape) entity;
					Stack right = (Stack) grounding;
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

	private static boolean matchEdge(WorldEntity entity, Relation relation,
			Edge right) {
		if (relation == Relation.above) {
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
}