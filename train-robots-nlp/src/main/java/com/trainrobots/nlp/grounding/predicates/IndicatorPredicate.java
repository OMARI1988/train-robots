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

import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Edge;
import com.trainrobots.nlp.scenes.WorldEntity;

public class IndicatorPredicate implements Predicate {

	private final SpatialIndicator indicator;

	public IndicatorPredicate(SpatialIndicator indicator) {
		this.indicator = indicator;
	}

	@Override
	public boolean match(WorldEntity entity) {

		// Edge.
		if (entity instanceof Edge) {
			Edge edge = (Edge) entity;
			return edge.indicator() == indicator;
		}

		// Corner.
		if (entity instanceof Corner) {
			Corner corner = (Corner) entity;
			switch (indicator) {
			case front:
				return corner == Corner.FrontLeft
						|| corner == Corner.FrontRight;
			case back:
				return corner == Corner.BackLeft || corner == Corner.BackRight;
			case left:
				return corner == Corner.FrontLeft || corner == Corner.BackLeft;
			case right:
				return corner == Corner.FrontRight
						|| corner == Corner.BackRight;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "(spatial-indicator: " + indicator + ")";
	}
}