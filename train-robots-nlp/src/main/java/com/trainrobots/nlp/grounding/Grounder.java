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
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.nlp.grounding.predicates.ColorPredicate;
import com.trainrobots.nlp.grounding.predicates.IndicatorPredicate;
import com.trainrobots.nlp.grounding.predicates.Predicate;
import com.trainrobots.nlp.grounding.predicates.PredicateList;
import com.trainrobots.nlp.grounding.predicates.RelationPredicate;
import com.trainrobots.nlp.grounding.predicates.TypePredicate;
import com.trainrobots.nlp.scenes.Corner;
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
	}

	public List<Grounding> ground(Entity entity) {

		// Predicates.
		PredicateList predicates = new PredicateList();

		// Reference ID.
		if (entity.referenceId() != null) {
			throw new CoreException("Unexpected reference ID: "
					+ entity.referenceId());
		}

		// Type.
		if (entity.type() == null) {
			throw new CoreException("Entity type not specified.");
		}
		predicates.add(new TypePredicate(entity.type()));

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
		if (entity.colors() != null) {
			if (entity.colors().size() != 1) {
				throw new CoreException("Expected single entity color.");
			}
			predicates.add(new ColorPredicate(entity.colors().get(0)));
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
			filterGroundings(groundings, relations);
		}
		return groundings;
	}

	private void filterGroundings(List<Grounding> groundings,
			List<SpatialRelation> relations) {

		// Predicates.
		PredicateList predicates = new PredicateList();
		for (SpatialRelation relation : relations) {
			Predicate predicate = createPredicateForRelation(relation);
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

	private Predicate createPredicateForRelation(SpatialRelation relation) {

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
		List<Grounding> groundings = ground(entity);

		// No match.
		return new RelationPredicate(indicator, groundings);
	}
}