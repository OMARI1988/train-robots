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

package com.trainrobots.nlp.grounding.predicates;

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.nlp.grounding.Grounding;
import com.trainrobots.nlp.scenes.Board;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.WorldEntity;

public class RelationPredicate implements Predicate {

	private final SpatialIndicator indicator;
	private final List<Grounding> groundings;

	public RelationPredicate(SpatialIndicator indicator,
			List<Grounding> groundings) {
		this.indicator = indicator;
		this.groundings = groundings;
	}

	@Override
	public boolean match(WorldEntity entity) {

		// Validate.
		if (indicator != SpatialIndicator.above) {
			throw new CoreException("Invalid relation predicate indicator: "
					+ indicator);
		}
		if (!(entity instanceof Shape)) {
			throw new CoreException(
					"Expected left hand side to be a shape in relation predicate.");
		}
		Shape left = (Shape) entity;

		// Match?
		for (Grounding grounding : groundings) {

			// Board.
			if (grounding.entity() instanceof Board) {
				if (left.position().z == 0) {
					return true;
				}
				continue;
			}

			// Shape.
			if (grounding.entity() instanceof Shape) {
				Shape right = (Shape) grounding.entity();
				if (left.position().equals(right.position().add(0, 0, 1))) {
					return true;
				}
				continue;
			}

			// Corner.
			if (grounding.entity() instanceof Corner) {
				Corner right = (Corner) grounding.entity();
				if (left.position().equals(right.position())) {
					return true;
				}
				continue;
			}
		}

		// No match.
		return false;
	}

	@Override
	public String toString() {
		return "(spatial-relation: " + indicator + ")";
	}
}