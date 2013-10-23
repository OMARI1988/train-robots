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

package com.trainrobots.nlp.planning;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Indicator;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Relation;
import com.trainrobots.core.rcl.RelationAttribute;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.planning.predicates.ColorPredicate;
import com.trainrobots.nlp.planning.predicates.IndicatorPredicate;
import com.trainrobots.nlp.planning.predicates.MeasurePredicate;
import com.trainrobots.nlp.planning.predicates.Predicate;
import com.trainrobots.nlp.planning.predicates.PredicateList;
import com.trainrobots.nlp.planning.predicates.RegionPredicate;
import com.trainrobots.nlp.planning.predicates.RelationPredicate;
import com.trainrobots.nlp.planning.predicates.TypePredicate;
import com.trainrobots.nlp.scenes.CenterOfBoard;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Edge;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Robot;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.DropMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.TakeMove;

public class Planner {

	private final Model model;

	public Planner(WorldModel world) {
		model = new Model(world);
	}

	public List<WorldEntity> ground(Rcl root, Entity entity) {

		// Predicates.
		PredicateList predicates = new PredicateList();

		// Validate.
		Type type = entity.typeAttribute().type();
		if (type == null) {
			throw new CoreException("Entity type not specified: " + entity);
		}

		// Type reference?
		if (type == Type.typeReference || type == Type.typeReferenceGroup) {
			if (entity.referenceId() == null) {
				throw new CoreException("Reference ID not specified: " + entity);
			}
			Entity antecedent = (Entity) root.getElement(entity.referenceId());
			if (antecedent == null) {
				throw new CoreException("Failed to resolve reference: "
						+ entity);
			}
			if (type == Type.typeReferenceGroup) {
				type = makeGroup(antecedent.typeAttribute().type());
			} else {
				type = antecedent.typeAttribute().type();
			}
		}

		// Reference ID.
		else if (entity.referenceId() != null) {
			throw new CoreException("Unexpected reference ID: "
					+ entity.referenceId());
		}

		// Cube group?
		if (type == Type.cubeGroup) {
			type = Type.stack;
		}

		// Type.
		predicates.add(new TypePredicate(type));

		// Ordinal.
		if (entity.ordinalAttribute() != null) {
			throw new CoreException("Unexpected ordinal: "
					+ entity.ordinalAttribute());
		}

		// Cardinal.
		if (entity.cardinalAttribute() != null
				&& entity.cardinalAttribute().cardinal() != 1) {
			throw new CoreException("Unexpected cardinal: "
					+ entity.cardinalAttribute());
		}

		// Colors.
		if (entity.colorAttributes() != null
				&& entity.colorAttributes().size() >= 1) {
			predicates.add(new ColorPredicate(entity.colorAttributes()));
		}

		// Indicator/relation combinations.
		List<IndicatorAttribute> indicatorAttributes = new ArrayList<IndicatorAttribute>();
		if (entity.indicatorAttributes() != null) {
			indicatorAttributes.addAll(entity.indicatorAttributes());
		}
		List<SpatialRelation> relations = new ArrayList<SpatialRelation>();
		if (entity.relations() != null) {
			relations.addAll(entity.relations());
		}

		// Indicators.
		Indicator postIndicator = null;
		for (IndicatorAttribute indicatorAttribute : indicatorAttributes) {
			Indicator indicator = indicatorAttribute.indicator();
			if ((type == Type.cube || type == Type.prism || type == Type.stack)
					&& (indicator == Indicator.left
							|| indicator == Indicator.leftmost
							|| indicator == Indicator.right
							|| indicator == Indicator.rightmost || indicator == Indicator.nearest)) {
				if (postIndicator != null) {
					throw new CoreException("Duplicate post indicator in "
							+ entity);
				}
				postIndicator = indicator;
			} else {
				predicates
						.add(new IndicatorPredicate(model.world(), indicator));
			}
		}

		// Apply predicates.
		List<WorldEntity> groundings = new ArrayList<WorldEntity>();
		for (WorldEntity worldEntity : model.entities()) {
			if (predicates.match(worldEntity)) {
				groundings.add(worldEntity);
			}
		}

		// Post-relation (e.g. 25886).
		boolean postRelation = false;
		if (relations.size() == 1) {
			SpatialRelation relation = relations.get(0);
			if (relation.entity() != null
					&& relation.entity().isType(Type.region)) {
				if (relation.entity().indicatorAttributes().size() == 1
						&& relation.entity().indicatorAttributes().get(0)
								.indicator() == Indicator.right) {
					if (postIndicator != null) {
						throw new CoreException("Duplicate post indicator in "
								+ entity);
					}
					postIndicator = relation.entity().indicatorAttributes()
							.get(0).indicator();
					postRelation = true;
				}
			}
		}

		// Relations.
		if (!postRelation && relations.size() > 0) {
			filterGroundingsByRelations(root, entity, groundings, relations);
		}

		// Post-indicator.
		if (postIndicator != null) {
			filterGroundingsByPostIndicator(groundings, postIndicator);
		}

		// Groundings.
		return groundings;
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

		Shape shape = model.world().getShapeInGripper();
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
			Position arm = model.world().arm();
			if (position2.x != arm.x || position2.y != arm.y) {
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

		if (model.world().getShapeInGripper() != null) {
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

	private Position mapSpatialRelationWithMeasure(Rcl root, Position position,
			SpatialRelation relation) {

		if (relation.entity() != null) {
			List<WorldEntity> groundings = ground(root, relation.entity());
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

	private WorldEntity mapEntity(Rcl root, Entity entity,
			Position excludePosition) {

		// Multiple groundings?
		List<WorldEntity> groundings = ground(root, entity);
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

	private static Type makeGroup(Type type) {
		if (type == Type.cube) {
			return Type.cubeGroup;
		}
		throw new CoreException("Failed to determine group type for '" + type
				+ "'.");
	}

	private void filterGroundingsByPostIndicator(List<WorldEntity> groundings,
			Indicator postIndicator) {

		if (postIndicator == Indicator.left
				|| postIndicator == Indicator.leftmost) {
			filterLeftmost(groundings);
			return;
		}

		if (postIndicator == Indicator.right
				|| postIndicator == Indicator.rightmost) {
			filterRightmost(groundings);
			return;
		}

		if (postIndicator == Indicator.nearest) {
			List<WorldEntity> landmarks = new ArrayList<WorldEntity>();
			landmarks.add(Robot.TheRobot);
			filterNearest(groundings, landmarks);
			return;
		}

		// No match.
		throw new CoreException("Post-indicator not supported: "
				+ postIndicator);
	}

	private void filterGroundingsByRelations(Rcl root, Entity parent,
			List<WorldEntity> groundings, List<SpatialRelation> relations) {

		// Nearest?
		if (relations.size() == 1) {
			SpatialRelation relation = relations.get(0);
			if (relation.relationAttribute().relation() == Relation.nearest) {
				filterNearest(root, groundings, relation);
				return;
			}
		}

		// Predicates.
		PredicateList predicates = new PredicateList();
		for (SpatialRelation relation : relations) {
			Predicate predicate = createPredicateForRelation(root, parent,
					relation);
			if (predicate != null) {
				predicates.add(predicate);
			}
		}

		// Filter.
		for (int i = groundings.size() - 1; i >= 0; i--) {
			if (!predicates.match(groundings.get(i))) {
				groundings.remove(i);
			}
		}
	}

	private void filterNearest(Rcl root, List<WorldEntity> groundings,
			SpatialRelation relation) {

		Entity entity = relation.entity();
		if (entity == null) {
			throw new CoreException("Spatial relation entity not specified: "
					+ relation);
		}

		List<WorldEntity> landmarks;
		if (entity.isType(Type.region)) {
			landmarks = new ArrayList<WorldEntity>();
			landmarks.add(getRegion(entity));
		} else {
			landmarks = ground(root, entity);
		}
		if (landmarks.size() == 0) {
			throw new CoreException("No landmarks for nearest relation: "
					+ landmarks.size() + " groundings: " + entity);
		}

		filterNearest(groundings, landmarks);
	}

	private void filterNearest(List<WorldEntity> groundings,
			List<WorldEntity> landmarks) {
		List<Double> distances = new ArrayList<Double>();
		double best = Double.MAX_VALUE;
		for (WorldEntity grounding : groundings) {
			double min = Double.MAX_VALUE;
			for (WorldEntity landmark : landmarks) {
				double distance = getDistance(grounding, landmark);
				if (distance < best) {
					best = distance;
				}
				if (distance < min) {
					min = distance;
				}
			}
			distances.add(min);
		}

		List<WorldEntity> copy = new ArrayList<WorldEntity>(groundings);
		groundings.clear();
		for (int i = 0; i < copy.size(); i++) {
			if (distances.get(i) == best) {
				groundings.add(copy.get(i));
			}
		}
	}

	private void filterLeftmost(List<WorldEntity> groundings) {

		List<Integer> metrics = new ArrayList<Integer>();
		int best = Integer.MIN_VALUE;
		for (WorldEntity grounding : groundings) {
			int metric = grounding.basePosition().y;
			if (metric > best) {
				best = metric;
			}
			metrics.add(metric);
		}

		List<WorldEntity> copy = new ArrayList<WorldEntity>(groundings);
		groundings.clear();
		for (int i = 0; i < copy.size(); i++) {
			if (metrics.get(i) == best) {
				groundings.add(copy.get(i));
			}
		}
	}

	private void filterRightmost(List<WorldEntity> groundings) {

		List<Integer> metrics = new ArrayList<Integer>();
		int best = Integer.MAX_VALUE;
		for (WorldEntity grounding : groundings) {
			int metric = grounding.basePosition().y;
			if (metric < best) {
				best = metric;
			}
			metrics.add(metric);
		}

		List<WorldEntity> copy = new ArrayList<WorldEntity>(groundings);
		groundings.clear();
		for (int i = 0; i < copy.size(); i++) {
			if (metrics.get(i) == best) {
				groundings.add(copy.get(i));
			}
		}
	}

	private WorldEntity getRegion(Entity entity) {
		if (entity.isType(Type.region) && entity.indicatorAttributes() != null
				&& entity.indicatorAttributes().size() == 1) {
			switch (entity.indicatorAttributes().get(0).indicator()) {
			case front:
				return Edge.Front;
			case back:
				return Edge.Back;
			case left:
				return Edge.Left;
			case right:
				return Edge.Right;
			case center:
				return new CenterOfBoard();
			}
		}
		throw new CoreException("Failed to convert region to edge: " + entity);
	}

	private static double getDistance(WorldEntity entity, WorldEntity landmark) {

		// Robot?
		if (landmark.type() == Type.robot) {
			return entity.basePosition().x;
		}

		// Edges?
		if (landmark.type() == Type.edge) {
			Edge edge = (Edge) landmark;
			switch (edge.indicator()) {
			case left:
				return 7 - entity.basePosition().y;
			case right:
				return entity.basePosition().y;
			case front:
				return 7 - entity.basePosition().x;
			case back:
				return entity.basePosition().x;
			default:
				throw new CoreException("Invalid edge: " + edge);
			}
		}

		// Distance.
		Position p1 = entity.basePosition();
		double p2x;
		double p2y;
		if (landmark instanceof CenterOfBoard) {
			p2x = 3.5;
			p2y = 3.5;
		} else {
			Position p2 = landmark.basePosition();
			p2x = p2.x;
			p2y = p2.y;
		}
		double dx = p1.x - p2x;
		double dy = p1.y - p2y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	private Predicate createPredicateForRelation(Rcl root, Entity parent,
			SpatialRelation relation) {

		// Relation.
		RelationAttribute relationAttribute = relation.relationAttribute();
		if (relationAttribute == null) {
			throw new CoreException("Spatial relation not specified: "
					+ relation);
		}

		// Measure?
		Entity measure = relation.measure();
		if (measure != null) {
			if (relation.entity() == null) {
				Event event = (Event) root;
				if (parent == event.entity()) {
					throw new CoreException("Circular reference.");
				}
				List<WorldEntity> landmarks = ground(root, event.entity());
				if (landmarks == null || landmarks.size() != 1) {
					throw new CoreException(
							"Failed to ground single landmark for measure relation: "
									+ event.entity());
				}
				return new MeasurePredicate(measure, relation
						.relationAttribute().relation(), landmarks.get(0));
			}
			throw new CoreException("Failed to create predicate for measure: "
					+ relation.measure());
		}

		// Entity.
		Entity entity = relation.entity();
		if (entity == null) {
			throw new CoreException("Spatial relation entity not specified: "
					+ relation);
		}

		// Region?
		if (entity.isType(Type.region) && entity.indicatorAttributes() != null
				&& entity.indicatorAttributes().size() >= 1) {
			return new RegionPredicate(relationAttribute,
					entity.indicatorAttributes());
		}

		// Groundings.
		List<WorldEntity> groundings = ground(root, entity);
		if (groundings.size() == 0) {
			throw new CoreException(
					"Entity in spatial relation has no groundings: " + entity);
		}
		return new RelationPredicate(relationAttribute.relation(), groundings);
	}
}