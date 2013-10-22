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

package com.trainrobots.nlp.grounding;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Indicator;
import com.trainrobots.core.rcl.Relation;
import com.trainrobots.core.rcl.RelationAttribute;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.grounding.predicates.ColorPredicate;
import com.trainrobots.nlp.grounding.predicates.IndicatorPredicate;
import com.trainrobots.nlp.grounding.predicates.MeasurePredicate;
import com.trainrobots.nlp.grounding.predicates.Predicate;
import com.trainrobots.nlp.grounding.predicates.PredicateList;
import com.trainrobots.nlp.grounding.predicates.RegionPredicate;
import com.trainrobots.nlp.grounding.predicates.RelationPredicate;
import com.trainrobots.nlp.grounding.predicates.TypePredicate;
import com.trainrobots.nlp.scenes.Board;
import com.trainrobots.nlp.scenes.CenterOfBoard;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Edge;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Robot;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;

public class Grounder {

	private final WorldModel world;
	private final List<WorldEntity> entities = new ArrayList<WorldEntity>();

	public Grounder(WorldModel world) {

		// World.
		this.world = world;

		// Shapes.
		for (Shape shape : world.shapes()) {
			entities.add(shape);
		}

		// Stacks.
		for (Stack stack : StackFinder.getStacks(world)) {
			entities.add(stack);
		}

		// Corners.
		entities.add(Corner.BackRight);
		entities.add(Corner.BackLeft);
		entities.add(Corner.FrontRight);
		entities.add(Corner.FrontLeft);

		// Edges.
		entities.add(Edge.Front);
		entities.add(Edge.Back);
		entities.add(Edge.Left);
		entities.add(Edge.Right);

		// Board.
		entities.add(Board.TheBoard);

		// Robot.
		entities.add(Robot.TheRobot);
	}

	public List<WorldEntity> ground(Rcl root, Entity entity) {
		return ground(root, entity, null);
	}

	public List<WorldEntity> ground(Rcl root, Entity entity,
			Position excludePosition) {

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
				predicates.add(new IndicatorPredicate(world, indicator));
			}
		}

		// Apply predicates.
		List<WorldEntity> groundings = new ArrayList<WorldEntity>();
		for (WorldEntity worldEntity : entities) {
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
			filterGroundingsByPostIndicator(groundings, postIndicator,
					excludePosition);
		}

		// Groundings.
		return groundings;
	}

	private static Type makeGroup(Type type) {
		if (type == Type.cube) {
			return Type.cubeGroup;
		}
		throw new CoreException("Failed to determine group type for '" + type
				+ "'.");
	}

	private void filterGroundingsByPostIndicator(List<WorldEntity> groundings,
			Indicator postIndicator, Position excludePosition) {

		// Tried to fix 21517, but failed for other reasons.
		if (groundings.size() > 1 && excludePosition != null) {
			for (int i = groundings.size() - 1; i >= 0; i--) {
				WorldEntity e = groundings.get(i);
				if (e instanceof Shape) {
					if (((Shape) e).position().equals(excludePosition)) {
						groundings.remove(i);
					}
				}
			}
		}

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