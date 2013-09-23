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
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.SpatialIndicator;
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

	public List<Grounding> ground(Rcl root, Entity entity) {
		return ground(root, entity, null);
	}

	public List<Grounding> ground(Rcl root, Entity entity,
			Position excludePosition) {

		// Predicates.
		PredicateList predicates = new PredicateList();

		// Type reference?
		Type type = entity.type();
		if (type == null) {
			throw new CoreException("Entity type not specified: " + entity);
		}
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
				type = makeGroup(antecedent.type());
			} else {
				type = antecedent.type();
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
		if (entity.ordinal() != null) {
			throw new CoreException("Unexpected ordinal: " + entity.ordinal());
		}

		// Cardinal.
		if (entity.cardinal() != null && entity.cardinal() != 1) {
			throw new CoreException("Unexpected cardinal: " + entity.cardinal());
		}

		// Colors.
		if (entity.colors() != null && entity.colors().size() >= 1) {
			predicates.add(new ColorPredicate(entity.colors()));
		}

		// Indicator/relation combinations.
		List<SpatialIndicator> indicators = new ArrayList<SpatialIndicator>();
		if (entity.indicators() != null) {
			indicators.addAll(entity.indicators());
		}
		List<SpatialRelation> relations = new ArrayList<SpatialRelation>();
		if (entity.relations() != null) {
			relations.addAll(entity.relations());
		}

		// Indicators.
		SpatialIndicator postIndicator = null;
		for (SpatialIndicator indicator : indicators) {
			if ((type == Type.cube || type == Type.prism || type == Type.stack)
					&& (indicator == SpatialIndicator.left
							|| indicator == SpatialIndicator.leftmost
							|| indicator == SpatialIndicator.right
							|| indicator == SpatialIndicator.rightmost || indicator == SpatialIndicator.nearest)) {
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
		List<Grounding> groundings = new ArrayList<Grounding>();
		for (WorldEntity worldEntity : entities) {
			if (predicates.match(worldEntity)) {
				groundings.add(new Grounding(worldEntity));
			}
		}

		// Post-relation (e.g. 25886).
		boolean postRelation = false;
		if (relations.size() == 1) {
			SpatialRelation relation = relations.get(0);
			if (relation.entity() != null
					&& relation.entity().type() == Type.region) {
				if (relation.entity().indicators().size() == 1
						&& relation.entity().indicators().get(0) == SpatialIndicator.right) {
					if (postIndicator != null) {
						throw new CoreException("Duplicate post indicator in "
								+ entity);
					}
					postIndicator = relation.entity().indicators().get(0);
					postRelation = true;
				}
			}
		}

		// Relations.
		if (!postRelation && relations.size() > 0) {
			filterGroundingsByRelations(root, groundings, relations);
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

	private void filterGroundingsByPostIndicator(List<Grounding> groundings,
			SpatialIndicator postIndicator, Position excludePosition) {

		// Tried to fix 21517, but failed for other reasons.
		if (groundings.size() > 1 && excludePosition != null) {
			for (int i = groundings.size() - 1; i >= 0; i--) {
				WorldEntity e = groundings.get(i).entity();
				if (e instanceof Shape) {
					if (((Shape) e).position().equals(excludePosition)) {
						groundings.remove(i);
					}
				}
			}
		}

		if (postIndicator == SpatialIndicator.left
				|| postIndicator == SpatialIndicator.leftmost) {
			filterLeftmost(groundings);
			return;
		}

		if (postIndicator == SpatialIndicator.right
				|| postIndicator == SpatialIndicator.rightmost) {
			filterRightmost(groundings);
			return;
		}

		if (postIndicator == SpatialIndicator.nearest) {
			List<Grounding> landmarks = new ArrayList<Grounding>();
			landmarks.add(new Grounding(Robot.TheRobot));
			filterNearest(groundings, landmarks);
			return;
		}

		// No match.
		throw new CoreException("Post-indicator not supported: "
				+ postIndicator);
	}

	private void filterGroundingsByRelations(Rcl root,
			List<Grounding> groundings, List<SpatialRelation> relations) {

		// Nearest?
		if (relations.size() == 1) {
			SpatialRelation relation = relations.get(0);
			if (relation.indicator() == SpatialIndicator.nearest) {
				filterNearest(root, groundings, relation);
				return;
			}
		}

		// Predicates.
		PredicateList predicates = new PredicateList();
		for (SpatialRelation relation : relations) {
			Predicate predicate = createPredicateForRelation(root, relation);
			if (predicate != null) {
				predicates.add(predicate);
			}
		}

		// Filter.
		for (int i = groundings.size() - 1; i >= 0; i--) {
			if (!predicates.match(groundings.get(i).entity())) {
				groundings.remove(i);
			}
		}
	}

	private void filterNearest(Rcl root, List<Grounding> groundings,
			SpatialRelation relation) {

		Entity entity = relation.entity();
		if (entity == null) {
			throw new CoreException("Spatial relation entity not specified: "
					+ relation);
		}

		List<Grounding> landmarks;
		if (entity.type() == Type.region) {
			landmarks = new ArrayList<Grounding>();
			landmarks.add(new Grounding(getRegion(entity)));
		} else {
			landmarks = ground(root, entity);
		}
		if (landmarks.size() == 0) {
			throw new CoreException("No landmarks for nearest relation: "
					+ landmarks.size() + " groundings: " + entity);
		}

		filterNearest(groundings, landmarks);
	}

	private void filterNearest(List<Grounding> groundings,
			List<Grounding> landmarks) {
		List<Double> distances = new ArrayList<Double>();
		double best = Double.MAX_VALUE;
		for (Grounding grounding : groundings) {
			double min = Double.MAX_VALUE;
			for (Grounding landmark : landmarks) {
				double distance = getDistance(grounding.entity(),
						landmark.entity());
				if (distance < best) {
					best = distance;
				}
				if (distance < min) {
					min = distance;
				}
			}
			distances.add(min);
		}

		List<Grounding> copy = new ArrayList<Grounding>(groundings);
		groundings.clear();
		for (int i = 0; i < copy.size(); i++) {
			if (distances.get(i) == best) {
				groundings.add(copy.get(i));
			}
		}
	}

	private void filterLeftmost(List<Grounding> groundings) {

		List<Integer> metrics = new ArrayList<Integer>();
		int best = Integer.MIN_VALUE;
		for (Grounding grounding : groundings) {
			int metric = grounding.entity().basePosition().y;
			if (metric > best) {
				best = metric;
			}
			metrics.add(metric);
		}

		List<Grounding> copy = new ArrayList<Grounding>(groundings);
		groundings.clear();
		for (int i = 0; i < copy.size(); i++) {
			if (metrics.get(i) == best) {
				groundings.add(copy.get(i));
			}
		}
	}

	private void filterRightmost(List<Grounding> groundings) {

		List<Integer> metrics = new ArrayList<Integer>();
		int best = Integer.MAX_VALUE;
		for (Grounding grounding : groundings) {
			int metric = grounding.entity().basePosition().y;
			if (metric < best) {
				best = metric;
			}
			metrics.add(metric);
		}

		List<Grounding> copy = new ArrayList<Grounding>(groundings);
		groundings.clear();
		for (int i = 0; i < copy.size(); i++) {
			if (metrics.get(i) == best) {
				groundings.add(copy.get(i));
			}
		}
	}

	private WorldEntity getRegion(Entity entity) {
		if (entity.type() == Type.region && entity.indicators() != null
				&& entity.indicators().size() == 1) {
			switch (entity.indicators().get(0)) {
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

	private Predicate createPredicateForRelation(Rcl root,
			SpatialRelation relation) {

		// Indicator.
		SpatialIndicator indicator = relation.indicator();
		if (indicator == null) {
			throw new CoreException(
					"Spatial relation indicator not specified: " + indicator);
		}

		// Measure?
		Entity measure = relation.measure();
		if (measure != null) {
			if (relation.entity() == null) {
				Event event = (Event) root;
				List<Grounding> landmarks = ground(root, event.entity());
				if (landmarks == null || landmarks.size() != 1) {
					throw new CoreException(
							"Failed to ground single landmark for measure relation: "
									+ event.entity());
				}
				return new MeasurePredicate(measure, relation.indicator(),
						landmarks.get(0).entity());
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
		if (entity.type() == Type.region && entity.indicators() != null
				&& entity.indicators().size() >= 1) {
			return new RegionPredicate(indicator, entity.indicators());
		}

		// Groundings.
		List<Grounding> groundings = ground(root, entity);
		if (groundings.size() == 0) {
			throw new CoreException(
					"Entity in spatial relation has no groundings: " + entity);
		}
		return new RelationPredicate(indicator, groundings);
	}
}