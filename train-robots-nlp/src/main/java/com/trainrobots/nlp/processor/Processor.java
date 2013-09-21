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
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.grounding.Grounder;
import com.trainrobots.nlp.grounding.Grounding;
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

public class Processor {

	private final Grounder grounder;
	private final WorldModel world;

	public Processor(WorldModel world) {
		this.world = world;
		grounder = new Grounder(world);
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
		if (event1.action() != Action.take || event1.destinations() == null
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
		if (event2.action() != Action.drop) {
			return null;
		}
		Entity entity2 = event2.entity();
		if (entity2.type() != Type.reference || entity2.referenceId() == null
				|| !entity2.referenceId().equals(id)) {
			return null;
		}

		// Translate equivalent move.
		Event event3 = new Event(Action.move, entity1, event2.destinations());
		return getMove(event3, event3);
	}

	private Move getMove(Rcl root, Event event) {

		// Drop.
		if (event.action() == Action.drop) {
			return mapDropCommand(root, event);
		}

		// Take.
		if (event.action() == Action.take) {
			return mapTakeCommand(root, event);
		}

		// Move.
		if (event.action() == Action.move) {
			return mapMoveCommand(root, event);
		}

		// No match.
		throw new CoreException("Event action '" + event.action()
				+ "' not recognized.");
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

		return new DropMove();
	}

	private Move mapTakeCommand(Rcl root, Event event) {

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		return new TakeMove(getPosition(mapEntity(root, entity, null)));
	}

	private Move mapMoveCommand(Rcl root, Event event) {

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		Position position = getPosition(mapEntity(root, entity, null));

		if (event.destinations() == null || event.destinations().size() != 1) {
			throw new CoreException("Single destination not specified.");
		}

		SpatialRelation destination = event.destinations().get(0);

		// Measure?
		Position position2;
		if (destination.measure() != null) {
			position2 = mapSpatialRelationWithMeasure(root, position,
					destination);
		} else {
			position2 = mapSpatialRelation(root, position, destination);
		}
		return new DirectMove(position, position2);
	}

	private Position mapSpatialRelation(Rcl root, Position actionPosition,
			SpatialRelation relation) {

		SpatialIndicator indicator = relation.indicator();
		if (indicator == null) {
			throw new CoreException("Indicator not specified.");
		}

		if (relation.entity() == null) {
			throw new CoreException("Spatial relation entity not specified: "
					+ relation);
		}
		WorldEntity entity = mapEntity(root, relation.entity(), actionPosition);

		// Stack?
		if (entity.type() == Type.stack) {
			if (indicator == SpatialIndicator.above) {
				Stack stack = (Stack) entity;
				return stack.getTop().position().add(0, 0, 1);
			}
			throw new CoreException("Invalid indicator for stack: " + indicator);
		}

		// Corner?
		if (entity.type() == Type.corner) {
			Position position = getPosition(entity);
			if (indicator == SpatialIndicator.within
					|| indicator == SpatialIndicator.above) {
				return world.getDropPosition(position.x, position.y);
			}
		}

		// Board?
		if (entity.type() == Type.board && indicator == SpatialIndicator.above) {
			return world.getDropPosition(actionPosition.x, actionPosition.y);
		}

		// Shape.
		Position p = null;
		switch (indicator) {
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
		throw new CoreException("Invalid indicator: " + indicator);
	}

	private Position mapSpatialRelationWithMeasure(Rcl root, Position position,
			SpatialRelation relation) {

		if (relation.entity() != null) {
			List<Grounding> groundings = grounder.ground(root,
					relation.entity());
			if (groundings == null || groundings.size() != 1) {
				throw new CoreException(
						"Expected single grounding for entity with measure: "
								+ relation.entity());
			}
			position = groundings.get(0).entity().basePosition();
		}

		Entity measure = relation.measure();
		if (measure == null) {
			throw new CoreException("Measure not specified: " + relation);
		}
		if (measure.type() != Type.tile) {
			throw new CoreException("Unsupported measure type: " + relation);
		}
		if (measure.cardinal() == null) {
			throw new CoreException("Cardinal not specified: " + relation);
		}
		int cardinal = measure.cardinal();

		SpatialIndicator indicator = relation.indicator();
		Position p;
		switch (indicator) {
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
			throw new CoreException("Unsupported indicator type: " + indicator);
		}
		return world.getDropPosition(p.x, p.y);
	}

	private WorldEntity mapEntity(Rcl root, Entity entity,
			Position excludePosition) {

		// Multiple groundings?
		List<Grounding> groundings = grounder.ground(root, entity,
				excludePosition);
		if (groundings.size() > 1) {

			// Match the shape in the gripper.
			Shape shape = world.getShapeInGripper();
			if (shape != null) {
				for (Grounding grounding : groundings) {
					if (grounding.entity() instanceof Shape) {
						if (grounding.entity().equals(shape)) {
							return shape;
						}
					}
				}
			}

			// Match a single shape that supports no others.
			shape = matchSingleUnsupportingShape(groundings);
			if (shape != null) {
				return shape;
			}

			// Exclude (e.g. 6652).
			if (excludePosition != null && groundings.size() == 2) {
				for (int i = 0; i < 2; i++) {
					if (groundings.get(i).entity() instanceof Shape
							&& ((Shape) groundings.get(i).entity()).position()
									.equals(excludePosition)) {
						return groundings.get(1 - i).entity();
					}
				}
			}

			// Exclude headless stacks.
			for (int i = groundings.size() - 1; i >= 0; i--) {
				if (groundings.get(i).entity() instanceof Stack) {
					Stack stack = (Stack) groundings.get(i).entity();
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
		return groundings.get(0).entity();
	}

	private Shape matchSingleUnsupportingShape(List<Grounding> groundings) {
		Shape match = null;
		for (Grounding grounding : groundings) {
			WorldEntity entity = grounding.entity();
			if (!(entity instanceof Shape)) {
				continue;
			}
			Shape shape = (Shape) entity;
			if (world.getShape(shape.position().add(0, 0, 1)) == null) {
				if (match != null) {
					return null;
				}
				match = shape;
			}
		}
		return match;
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