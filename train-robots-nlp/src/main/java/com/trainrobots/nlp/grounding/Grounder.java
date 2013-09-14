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
import com.trainrobots.nlp.grounding.predicates.ColorPredicate;
import com.trainrobots.nlp.grounding.predicates.IndicatorPredicate;
import com.trainrobots.nlp.grounding.predicates.PredicateList;
import com.trainrobots.nlp.grounding.predicates.TypePredicate;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;

public class Grounder {

	private final List<WorldEntity> entities = new ArrayList<WorldEntity>();

	public Grounder(WorldModel world) {

		// Shapes.
		for (Shape shape : world.shapes()) {
			entities.add(shape);
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

		// Indicators.
		if (entity.indicators() != null) {
			for (SpatialIndicator indicator : entity.indicators()) {
				predicates.add(new IndicatorPredicate(indicator));
			}
		}

		// Relations.
		if (entity.relations() != null) {
			throw new CoreException("Unexpected entity relations in " + entity);
		}

		// Apply predicates.
		List<Grounding> list = new ArrayList<Grounding>();
		for (WorldEntity worldEntity : entities) {
			if (predicates.match(worldEntity)) {
				list.add(new Grounding(worldEntity));
			}
		}
		return list;
	}
}