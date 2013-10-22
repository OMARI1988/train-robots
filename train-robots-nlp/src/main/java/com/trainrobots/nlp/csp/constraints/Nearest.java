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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.scenes.CenterOfBoard;
import com.trainrobots.nlp.scenes.Edge;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.WorldEntity;

public class Nearest {

	private Nearest() {
	}

	public static List<WorldEntity> filterNearest(List<WorldEntity> entities,
			List<WorldEntity> landmarks) {

		List<Double> distances = new ArrayList<Double>();
		double best = Double.MAX_VALUE;
		for (WorldEntity entity : entities) {
			double min = Double.MAX_VALUE;
			for (WorldEntity landmark : landmarks) {
				double distance = getDistance(entity, landmark);
				if (distance < best) {
					best = distance;
				}
				if (distance < min) {
					min = distance;
				}
			}
			distances.add(min);
		}

		List<WorldEntity> result = new ArrayList<WorldEntity>();
		for (int i = 0; i < entities.size(); i++) {
			if (distances.get(i) == best) {
				result.add(entities.get(i));
			}
		}
		return result;
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
}