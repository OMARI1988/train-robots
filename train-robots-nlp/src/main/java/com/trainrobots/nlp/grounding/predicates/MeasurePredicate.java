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

import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.WorldEntity;

public class MeasurePredicate implements Predicate {

	private final Entity measure;
	private final SpatialIndicator indicator;
	private final WorldEntity landmark;

	public MeasurePredicate(Entity measure, SpatialIndicator indicator,
			WorldEntity landmark) {
		this.measure = measure;
		this.indicator = indicator;
		this.landmark = landmark;
	}

	@Override
	public boolean match(WorldEntity entity) {

		// Tiles.
		if (measure.isType(Type.tile) && measure.cardinalAttribute() != null) {

			Position p = entity.basePosition();
			Position l = landmark.basePosition();
			int n = measure.cardinalAttribute().cardinal();

			switch (indicator) {
			case right:
				return p.x == l.x && p.y == l.y - n;
			}
		}

		// No match.
		return false;
	}

	@Override
	public String toString() {
		return "(measure: " + measure + " " + indicator + " " + landmark + ")";
	}
}