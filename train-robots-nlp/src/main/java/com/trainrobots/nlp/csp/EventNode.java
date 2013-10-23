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

package com.trainrobots.nlp.csp;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Relation;
import com.trainrobots.core.rcl.RelationAttribute;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.DropMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.TakeMove;

public class EventNode implements ActionNode {

	private final Rcl root;
	private final Event event;

	public EventNode(Rcl root, Event event) {
		this.root = root;
		this.event = event;
	}

	public List<Move> solve(Model model) {

		// Moves.
		ArrayList<Move> moves = new ArrayList<Move>();

		// Drop.
		if (event.isAction(Action.drop)) {
			moves.add(mapDropCommand(model));
			return moves;
		}

		// Take.
		if (event.isAction(Action.take)) {
			moves.add(mapTakeCommand(model));
			return moves;
		}

		// Move.
		if (event.isAction(Action.move)) {
			moves.add(mapMoveCommand(model));
			return moves;
		}

		// No match.
		throw new CoreException("Event action '"
				+ event.actionAttribute().action() + "' not recognized.");
	}

	private Move mapDropCommand(Model model) {

		Shape shape = model.world().getShapeInGripper();
		if (shape == null) {
			throw new CoreException("Not holding anything to drop.");
		}

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		WorldEntity worldEntity = mapEntity(model, entity, null);
		if (!worldEntity.equals(shape)) {
			throw new CoreException("Drop shape mismatch.");
		}

		if (event.destinations() != null && event.destinations().size() >= 1) {
			Position position2 = mapDestination(model, shape.position());
			Position arm = model.world().arm();
			if (position2.x != arm.x || position2.y != arm.y) {
				throw new CoreException("Invalid drop position.");
			}
		}

		return new DropMove();
	}

	private Move mapTakeCommand(Model model) {

		if (event.destinations() != null && event.destinations().size() != 0) {
			throw new CoreException(
					"Take command must not have destination specified.");
		}

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		return new TakeMove(getPosition(mapEntity(model, entity, null)));
	}

	private Move mapMoveCommand(Model model) {

		if (model.world().getShapeInGripper() != null) {
			throw new CoreException("Expected drop not move.");
		}

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		Position position = getPosition(mapEntity(model, entity, null));
		Position position2 = mapDestination(model, position);
		return new DirectMove(position, position2);
	}

	private Position mapDestination(Model model, Position actionPosition) {
		if (event.destinations() == null || event.destinations().size() != 1) {
			throw new CoreException("Single destination not specified.");
		}

		SpatialRelation destination = event.destinations().get(0);

		// Measure?
		if (destination.measure() != null) {
			return mapSpatialRelationWithMeasure(model, actionPosition,
					destination);
		}
		return mapSpatialRelation(model, actionPosition, destination);
	}

	private Position mapSpatialRelation(Model model, Position actionPosition,
			SpatialRelation relation) {

		RelationAttribute relationAttribute = relation.relationAttribute();
		if (relationAttribute == null) {
			throw new CoreException("Relation not specified.");
		}

		if (relation.entity() == null) {
			throw new CoreException("Spatial relation entity not specified: "
					+ relation);
		}
		WorldEntity entity = mapEntity(model, relation.entity(), actionPosition);

		// Stack?
		if (entity.type() == Type.stack) {
			if (relationAttribute.relation() == Relation.above) {
				Stack stack = (Stack) entity;
				return stack.getTop().position().add(0, 0, 1);
			}
			throw new CoreException("Invalid relation for stack: "
					+ relationAttribute);
		}

		// Corner?
		if (entity.type() == Type.corner) {
			Position position = getPosition(entity);
			if (relationAttribute.relation() == Relation.within
					|| relationAttribute.relation() == Relation.above) {
				return model.world().getDropPosition(position.x, position.y);
			}
		}

		// Board?
		if (entity.type() == Type.board
				&& relationAttribute.relation() == Relation.above) {
			return model.world().getDropPosition(actionPosition.x,
					actionPosition.y);
		}

		// Shape.
		Position p = null;
		switch (relationAttribute.relation()) {
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
		throw new CoreException("Invalid relation: " + relationAttribute);
	}

	private Position mapSpatialRelationWithMeasure(Model model,
			Position position, SpatialRelation relation) {

		if (relation.entity() != null) {
			List<WorldEntity> groundings = ground(model, relation.entity());
			if (groundings == null || groundings.size() != 1) {
				throw new CoreException(
						"Expected single grounding for entity with measure: "
								+ relation.entity());
			}
			position = groundings.get(0).basePosition();
		}

		Entity measure = relation.measure();
		if (measure == null) {
			throw new CoreException("Measure not specified: " + relation);
		}
		if (!measure.isType(Type.tile)) {
			throw new CoreException("Unsupported measure type: " + relation);
		}
		if (measure.cardinalAttribute() == null) {
			throw new CoreException("Cardinal not specified: " + relation);
		}
		int cardinal = measure.cardinalAttribute().cardinal();

		RelationAttribute relationAttribute = relation.relationAttribute();
		Position p;
		switch (relationAttribute.relation()) {
		case left:
			p = position.add(0, cardinal, 0);
			break;
		case right:
			p = position.add(0, -cardinal, 0);
			break;
		case forward:
			p = position.add(cardinal, 0, 0);
			break;
		case backward:
			p = position.add(-cardinal, 0, 0);
			break;
		default:
			throw new CoreException("Unsupported relation: "
					+ relationAttribute);
		}
		return model.world().getDropPosition(p.x, p.y);
	}

	private WorldEntity mapEntity(Model model, Entity entity,
			Position excludePosition) {

		// Multiple groundings?
		List<WorldEntity> groundings = ground(model, entity);
		if (groundings.size() > 1) {

			// Match the shape in the gripper.
			Shape shape = model.world().getShapeInGripper();
			if (shape != null) {
				for (WorldEntity grounding : groundings) {
					if (grounding instanceof Shape) {
						if (grounding.equals(shape)) {
							return shape;
						}
					}
				}
			}

			// Remove shapes that support others.
			removeSupportingShapes(model, groundings);

			// Exclude (e.g. 6652).
			if (excludePosition != null && groundings.size() == 2) {
				for (int i = 0; i < 2; i++) {
					if (groundings.get(i) instanceof Shape
							&& ((Shape) groundings.get(i)).position().equals(
									excludePosition)) {
						return groundings.get(1 - i);
					}
				}
			}

			// Exclude headless stacks.
			for (int i = groundings.size() - 1; i >= 0; i--) {
				if (groundings.get(i) instanceof Stack) {
					Stack stack = (Stack) groundings.get(i);
					if (!stack.includesHead()) {
						groundings.remove(i);
					}
				}
			}
		}

		// Otherwise, we expect a single grounding.
		if (groundings.size() != 1) {
			throw new CoreException(groundings.size() + " grounding(s) : "
					+ entity);
		}
		return groundings.get(0);
	}

	private static void removeSupportingShapes(Model model,
			List<WorldEntity> groundings) {
		for (int i = groundings.size() - 1; i >= 0; i--) {
			WorldEntity entity = groundings.get(i);
			if (entity instanceof Shape) {
				Shape shape = (Shape) entity;
				if (model.world().getShape(shape.position().add(0, 0, 1)) != null) {
					groundings.remove(i);
				}
			}
		}
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

	private List<WorldEntity> ground(Model model, Entity entity) {
		return Csp.fromEntity(root, entity).solve(model);
	}
}