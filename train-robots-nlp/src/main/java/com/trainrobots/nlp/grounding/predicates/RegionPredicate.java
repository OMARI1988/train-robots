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
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.WorldEntity;

public class RegionPredicate implements Predicate {

	private final SpatialIndicator relation;
	private final SpatialIndicator region;

	public RegionPredicate(SpatialIndicator relation,
			List<SpatialIndicator> regions) {
		this.relation = relation;
		if (regions.size() != 1) {
			throw new CoreException("Incorrectly specified region.");
		}
		this.region = regions.get(0);
	}

	@Override
	public boolean match(WorldEntity entity) {

		// Within center.
		if (relation == SpatialIndicator.within
				&& region == SpatialIndicator.center) {
			Position p = entity.basePosition();
			if (p.x >= 3 && p.x <= 4 && p.y >= 3 && p.y <= 4) {
				return true;
			}
		}

		// No match.
		return false;
	}

	@Override
	public String toString() {
		return "(" + region + " region: " + relation + ")";
	}
}