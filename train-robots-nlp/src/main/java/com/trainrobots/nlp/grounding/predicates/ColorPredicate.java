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

import com.trainrobots.core.rcl.Color;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.WorldEntity;

public class ColorPredicate implements Predicate {

	private final Color color;

	public ColorPredicate(Color color) {
		this.color = color;
	}

	@Override
	public boolean match(WorldEntity entity) {
		if (!(entity instanceof Shape)) {
			return false;
		}
		Shape shape = (Shape) entity;
		return shape.color() == color;
	}

	@Override
	public String toString() {
		return "(color: " + color + ")";
	}
}