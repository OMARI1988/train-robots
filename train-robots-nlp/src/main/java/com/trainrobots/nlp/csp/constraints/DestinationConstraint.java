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

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Relation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.csp.EntityNode;
import com.trainrobots.nlp.csp.Model;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;

public class DestinationConstraint implements PositionConstraint {

	private final Relation relation;
	private final EntityNode entity;

	public DestinationConstraint(Relation relation, EntityNode entity) {
		this.relation = relation;
		this.entity = entity;
	}

	public Position solve(Model model, Position actionPosition) {

		List<WorldEntity> groundings = entity.solve(model);
		Available.filterAvailable(model, groundings);

		// Exclude (e.g. 6652).
		if (actionPosition != null && groundings.size() == 2) {
			for (int i = 0; i < 2; i++) {
				if (groundings.get(i) instanceof Shape
						&& ((Shape) groundings.get(i)).position().equals(
								actionPosition)) {
					WorldEntity g = groundings.get(1 - i);
					groundings.clear();
					groundings.add(g);
					break;
				}
			}
		}
		if (groundings.size() != 1) {
			throw new CoreException("Expected one grounding.");
		}
		WorldEntity entity = groundings.get(0);

		// Stack?
		if (entity.type() == Type.stack) {
			if (relation == Relation.above) {
				Stack stack = (Stack) entity;
				return stack.getTop().position().add(0, 0, 1);
			}
			throw new CoreException("Invalid relation for stack: " + relation);
		}

		// Corner?
		if (entity.type() == Type.corner) {
			Position position = getPosition(entity);
			if (relation == Relation.within || relation == Relation.above) {
				return model.world().getDropPosition(position.x, position.y);
			}
		}

		// Board?
		if (entity.type() == Type.board && relation == Relation.above) {
			return model.world().getDropPosition(actionPosition.x,
					actionPosition.y);
		}

		// Shape.
		Position p = null;
		switch (relation) {
		case left:
			p = getPosition(entity).add(0, 1, 0);
			break;
		case right:
			p = getPosition(entity).add(0, -1, 0);
			break;
		case front:
			p = getPosition(entity).add(1, 0, 0);
			break;
		case above:
			return getPosition(entity).add(0, 0, 1);
		}
		if (p != null) {
			return model.world().getDropPosition(p.x, p.y);
		}
		throw new CoreException("Invalid relation: " + relation);
	}

	private static Position getPosition(WorldEntity entity) {

		if (entity instanceof Shape) {
			return ((Shape) entity).position();
		}

		if (entity instanceof Corner) {
			return ((Corner) entity).basePosition();
		}

		throw new CoreException("Failed to get position for " + entity);
	}
}