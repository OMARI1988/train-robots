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

import com.trainrobots.core.CoreException;
import com.trainrobots.nlp.csp.constraints.CspConstraint;
import com.trainrobots.nlp.planning.Model;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;

public class CspSolver {

	private final Model model;

	public CspSolver(WorldModel world) {
		model = new Model(world);
	}

	public List<WorldEntity> solve(Csp csp) {
		if (csp.variableCount() != 1) {
			throw new CoreException("Failed to solve CSP.");
		}
		return solve(csp.getVariable(1));
	}

	private List<WorldEntity> solve(CspVariable variable) {
		List<WorldEntity> entities = model.entities();
		for (CspConstraint constraint : variable.constraints()) {
			entities = constraint.filter(model, entities);
		}
		return entities;
	}
}