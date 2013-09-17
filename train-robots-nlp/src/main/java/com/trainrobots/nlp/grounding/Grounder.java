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
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.grounding.predicates.ColorPredicate;
import com.trainrobots.nlp.grounding.predicates.IndicatorPredicate;
import com.trainrobots.nlp.grounding.predicates.Predicate;
import com.trainrobots.nlp.grounding.predicates.PredicateList;
import com.trainrobots.nlp.grounding.predicates.RelationPredicate;
import com.trainrobots.nlp.grounding.predicates.TypePredicate;
import com.trainrobots.nlp.scenes.Board;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Edge;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Robot;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;

public class Grounder {

	private final List<WorldEntity> entities = new ArrayList<WorldEntity>();

	public Grounder(WorldModel world) {

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

		// Predicates.
		PredicateList predicates = new PredicateList();

		// Type reference?
		Type type = entity.type();
		if (type == null) {
			throw new CoreException("Entity type not specified: " + entity);
		}
		if (type == Type.typeReference) {
			if (entity.referenceId() == null) {
				throw new CoreException("Reference ID not specified: " + entity);
			}
			Entity antecedent = (Entity) root.getElement(entity.referenceId());
			if (antecedent == null) {
				throw new CoreException("Failed to resolve reference: "
						+ entity);
			}
			type = antecedent.type();
		}

		// Reference ID.
		else if (entity.referenceId() != null) {
			throw new CoreException("Unexpected reference ID: "
					+ entity.referenceId());
		}

		// Type.
		predicates.add(new TypePredicate(type));

		// Ordinal.
		if (entity.ordinal() != null) {
			throw new CoreException("Unexpected ordinal: " + entity.ordinal());
		}

		// Cardinal.
		if (entity.cardinal() != null) {
			throw new CoreException("Unexpected cardinal: " + entity.cardinal());
		}

		// Multiple.
		if (entity.multiple()) {
			throw new CoreException("Unexpected multiple attribute.");
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
		for (SpatialIndicator indicator : indicators) {
			predicates.add(new IndicatorPredicate(indicator));
		}

		// Apply predicates.
		List<Grounding> groundings = new ArrayList<Grounding>();
		for (WorldEntity worldEntity : entities) {
			if (predicates.match(worldEntity)) {
				groundings.add(new Grounding(worldEntity));
			}
		}

		// Relations.
		if (relations.size() > 0) {
			filterGroundings(root, groundings, relations);
		}
		return groundings;
	}

	private void filterGroundings(Rcl root, List<Grounding> groundings,
			List<SpatialRelation> relations) {

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

		List<Grounding> landmarks = ground(root, entity);
		if (landmarks.size() == 0) {
			throw new CoreException("No landmarks for nearest relation: "
					+ landmarks.size() + " groundings: " + entity);
		}

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

		// Default.
		Position p1 = entity.basePosition();
		Position p2 = landmark.basePosition();
		if (p1.z != 0 || p2.z != 0) {
			throw new CoreException("Invalid base position.");
		}
		int dx = p1.x - p2.x;
		int dy = p1.y - p2.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	private Predicate createPredicateForRelation(Rcl root,
			SpatialRelation relation) {

		// Measure.
		if (relation.measure() != null) {
			throw new CoreException("Failed to create predicate for measure: "
					+ relation.measure());
		}

		// Indicator.
		SpatialIndicator indicator = relation.indicator();
		if (indicator == null) {
			throw new CoreException(
					"Spatial relation indicator not specified: " + indicator);
		}

		// Entity.
		Entity entity = relation.entity();
		if (entity == null) {
			throw new CoreException("Spatial relation entity not specified: "
					+ relation);
		}
		List<Grounding> groundings = ground(root, entity);
		if (groundings.size() == 0) {
			throw new CoreException(
					"Entity in spatial relation has no groundings: " + entity);
		}

		// Predicate.
		return new RelationPredicate(indicator, groundings);
	}
}