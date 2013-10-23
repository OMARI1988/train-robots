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

package com.trainrobots.nlp.csp.constraints;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Indicator;
import com.trainrobots.nlp.planning.Model;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Edge;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.WorldEntity;

public class IndicatorConstraint extends EntityConstraint {

	private final Indicator indicator;

	public IndicatorConstraint(Indicator indicator) {
		this.indicator = indicator;
	}

	public Indicator indicator() {
		return indicator;
	}

	@Override
	public List<WorldEntity> filter(Model model, List<WorldEntity> entities) {

		// Default.
		List<WorldEntity> result = new ArrayList<WorldEntity>();
		for (WorldEntity entity : entities) {
			if (match(model, entity)) {
				result.add(entity);
			}
		}
		return result;
	}

	@Override
	public Node toNode() {
		return new Node("indicator:", indicator.toString());
	}

	private boolean match(Model model, WorldEntity entity) {

		// Shape.
		if (indicator == Indicator.individual && entity instanceof Shape) {
			Shape shape = (Shape) entity;
			Position p = shape.position();
			return p.z == 0 && model.world().getShape(p.add(0, 0, 1)) == null;
		}

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
}