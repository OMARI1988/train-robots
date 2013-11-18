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

package com.trainrobots.nlp.csp;

import java.util.List;

import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;

public class Planner {

	private final Model model;

	public Planner(WorldModel world) {
		this.model = new Model(world);
	}

	public boolean isValidEvent(Rcl rcl) {
		try {
			Event event = (Event) rcl;
			Csp.fromAction(event, event).solve(model);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	public List<WorldEntity> getGroundings(Entity entity) {
		EntityNode entityNode = Csp.fromEntity(null, entity);
		return entityNode.solve(model);
	}
}