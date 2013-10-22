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
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Indicator;
import com.trainrobots.nlp.planning.Model;
import com.trainrobots.nlp.scenes.Robot;
import com.trainrobots.nlp.scenes.WorldEntity;

public class PostIndicatorConstraint extends CspConstraint {

	private final Indicator indicator;

	public PostIndicatorConstraint(Indicator indicator) {
		this.indicator = indicator;
	}

	public Indicator indicator() {
		return indicator;
	}

	@Override
	public List<WorldEntity> filter(Model model, List<WorldEntity> entities) {

		if (indicator == Indicator.left || indicator == Indicator.leftmost) {
			return filterLeftmost(entities);
		}

		if (indicator == Indicator.right || indicator == Indicator.rightmost) {
			return filterRightmost(entities);
		}

		if (indicator == Indicator.nearest) {
			List<WorldEntity> landmarks = new ArrayList<WorldEntity>();
			landmarks.add(Robot.TheRobot);
			return Nearest.filterNearest(entities, landmarks);
		}

		throw new CoreException("Post-indicator not supported: " + indicator);
	}

	@Override
	public Node toNode() {
		return new Node("post-indicator:", indicator.toString());
	}

	private static List<WorldEntity> filterLeftmost(List<WorldEntity> entities) {

		List<Integer> metrics = new ArrayList<Integer>();
		int best = Integer.MIN_VALUE;
		for (WorldEntity entity : entities) {
			int metric = entity.basePosition().y;
			if (metric > best) {
				best = metric;
			}
			metrics.add(metric);
		}

		List<WorldEntity> result = new ArrayList<WorldEntity>();
		for (int i = 0; i < entities.size(); i++) {
			if (metrics.get(i) == best) {
				result.add(entities.get(i));
			}
		}
		return result;
	}

	private static List<WorldEntity> filterRightmost(List<WorldEntity> entities) {

		List<Integer> metrics = new ArrayList<Integer>();
		int best = Integer.MAX_VALUE;
		for (WorldEntity entity : entities) {
			int metric = entity.basePosition().y;
			if (metric < best) {
				best = metric;
			}
			metrics.add(metric);
		}

		List<WorldEntity> result = new ArrayList<WorldEntity>();
		for (int i = 0; i < entities.size(); i++) {
			if (metrics.get(i) == best) {
				result.add(entities.get(i));
			}
		}
		return result;
	}
}