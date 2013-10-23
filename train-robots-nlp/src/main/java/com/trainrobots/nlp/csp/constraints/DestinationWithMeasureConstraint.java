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
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.WorldEntity;

public class DestinationWithMeasureConstraint implements PositionConstraint {

	private final Type type;
	private final int cardinal;
	private final Relation relation;
	private final EntityNode entity;

	public DestinationWithMeasureConstraint(Type type, int cardinal,
			Relation relation, EntityNode entity) {
		this.type = type;
		this.cardinal = cardinal;
		this.relation = relation;
		this.entity = entity;
	}

	public Position solve(Model model, Position actionPosition) {

		if (entity != null) {
			List<WorldEntity> groundings = entity.solve(model);
			if (groundings == null || groundings.size() != 1) {
				throw new CoreException(
						"Expected single grounding for entity with measure.");
			}
			actionPosition = groundings.get(0).basePosition();
		}

		if (type != Type.tile) {
			throw new CoreException("Unsupported measure type: " + relation);
		}

		Position p;
		switch (relation) {
		case left:
			p = actionPosition.add(0, cardinal, 0);
			break;
		case right:
			p = actionPosition.add(0, -cardinal, 0);
			break;
		case forward:
			p = actionPosition.add(cardinal, 0, 0);
			break;
		case backward:
			p = actionPosition.add(-cardinal, 0, 0);
			break;
		default:
			throw new CoreException("Unsupported relation: " + relation);
		}
		return model.world().getDropPosition(p.x, p.y);
	}
}