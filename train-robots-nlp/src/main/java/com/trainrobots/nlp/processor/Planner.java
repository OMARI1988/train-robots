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

package com.trainrobots.nlp.processor;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Relation;
import com.trainrobots.core.rcl.RelationAttribute;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.grounding.Grounder;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.DropMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.TakeMove;

public class Planner {

	private final Grounder grounder;
	private final WorldModel world;

	public Planner(WorldModel world) {
		this.world = world;
		grounder = new Grounder(world);
	}

	public Grounder grounder() {
		return grounder;
	}

	public List<Move> getMoves(Rcl rcl) {

		// Sequence.
		List<Move> moves = new ArrayList<Move>();
		if (rcl instanceof Sequence) {

			// Recognized sequence?
			Sequence sequence = (Sequence) rcl;
			Move move = matchRecognizedSequence(rcl, sequence);
			if (move != null) {
				moves.add(move);
				return moves;
			}

			// Default sequence handling.
			for (Event event : sequence.events()) {
				moves.add(getMove(rcl, event));
			}
		}

		// Event.
		else {
			if (!(rcl instanceof Event)) {
				throw new CoreException("Expected an RCL event.");
			}
			moves.add(getMove(rcl, (Event) rcl));
		}
		return moves;
	}

	private Move matchRecognizedSequence(Rcl root, Sequence sequence) {

		// Events.
		List<Event> events = sequence.events();
		if (events.size() != 2) {
			return null;
		}

		// Take.
		Event event1 = events.get(0);
		if (!event1.isAction(Action.take) || event1.destinations() == null
				|| event1.destinations().size() > 0) {
			return null;
		}
		Entity entity1 = event1.entity();
		Integer id = entity1.id();
		if (id == null) {
			return null;
		}

		// Drop.
		Event event2 = events.get(1);
		if (!event2.isAction(Action.drop)) {
			return null;
		}
		Entity entity2 = event2.entity();
		if (!entity2.isType(Type.reference) || entity2.referenceId() == null
				|| !entity2.referenceId().equals(id)) {
			return null;
		}

		// Validate.
		if (entity2.relations() != null && entity2.relations().size() >= 1) {
			throw new CoreException("References must not have relations: "
					+ entity2);
		}

		// Translate equivalent move.
		Event event3 = new Event(new ActionAttribute(Action.move), entity1,
				event2.destinations());
		return getMove(event3, event3);
	}

	private Move getMove(Rcl root, Event event) {

		// Drop.
		if (event.isAction(Action.drop)) {
			return mapDropCommand(root, event);
		}

		// Take.
		if (event.isAction(Action.take)) {
			return mapTakeCommand(root, event);
		}

		// Move.
		if (event.isAction(Action.move)) {
			return mapMoveCommand(root, event);
		}

		// No match.
		throw new CoreException("Event action '"
				+ event.actionAttribute().action() + "' not recognized.");
	}

	private Move mapDropCommand(Rcl root, Event event) {

		Shape shape = world.getShapeInGripper();
		if (shape == null) {
			throw new CoreException("Not holding anything to drop.");
		}

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		WorldEntity worldEntity = mapEntity(root, entity, null);
		if (!worldEntity.equals(shape)) {
			throw new CoreException("Drop shape mismatch.");
		}

		if (event.destinations() != null && event.destinations().size() >= 1) {
			Position position2 = mapDestination(root, event, shape.position());
			if (position2.x != world.arm().x || position2.y != world.arm().y) {
				throw new CoreException("Invalid drop position.");
			}
		}

		return new DropMove();
	}

	private Move mapTakeCommand(Rcl root, Event event) {

		if (event.destinations() != null && event.destinations().size() != 0) {
			throw new CoreException(
					"Take command must not have destination specified.");
		}

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		return new TakeMove(getPosition(mapEntity(root, entity, null)));
	}

	private Move mapMoveCommand(Rcl root, Event event) {

		if (world.getShapeInGripper() != null) {
			throw new CoreException("Expected drop not move.");
		}

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		Position position = getPosition(mapEntity(root, entity, null));
		Position position2 = mapDestination(root, event, position);
		return new DirectMove(position, position2);
	}

	private Position mapDestination(Rcl root, Event event,
			Position actionPosition) {
		if (event.destinations() == null || event.destinations().size() != 1) {
			throw new CoreException("Single destination not specified.");
		}

		SpatialRelation destination = event.destinations().get(0);

		// Measure?
		if (destination.measure() != null) {
			return mapSpatialRelationWithMeasure(root, actionPosition,
					destination);
		}
		return mapSpatialRelation(root, actionPosition, destination);
	}

	private Position mapSpatialRelation(Rcl root, Position actionPosition,
			SpatialRelation relation) {

		RelationAttribute relationAttribute = relation.relationAttribute();
		if (relationAttribute == null) {
			throw new CoreException("Relation not specified.");
		}

		if (relation.entity() == null) {
			throw new CoreException("Spatial relation entity not specified: "
					+ relation);
		}
		WorldEntity entity = mapEntity(root, relation.entity(), actionPosition);

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
				return world.getDropPosition(position.x, position.y);
			}
		}

		// Board?
		if (entity.type() == Type.board
				&& relationAttribute.relation() == Relation.above) {
			return world.getDropPosition(actionPosition.x, actionPosition.y);
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
			return world.getDropPosition(p.x, p.y);
		}
		throw new CoreException("Invalid relation: " + relationAttribute);
	}

	private Position mapSpatialRelationWithMeasure(Rcl root, Position position,
			SpatialRelation relation) {

		if (relation.entity() != null) {
			List<WorldEntity> groundings = grounder.ground(root,
					relation.entity());
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
		return world.getDropPosition(p.x, p.y);
	}

	private WorldEntity mapEntity(Rcl root, Entity entity,
			Position excludePosition) {

		// Multiple groundings?
		List<WorldEntity> groundings = grounder.ground(root, entity,
				excludePosition);
		if (groundings.size() > 1) {

			// Match the shape in the gripper.
			Shape shape = world.getShapeInGripper();
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
			removeSupportingShapes(groundings);

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

	private void removeSupportingShapes(List<WorldEntity> groundings) {
		for (int i = groundings.size() - 1; i >= 0; i--) {
			WorldEntity entity = groundings.get(i);
			if (entity instanceof Shape) {
				Shape shape = (Shape) entity;
				if (world.getShape(shape.position().add(0, 0, 1)) != null) {
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
}