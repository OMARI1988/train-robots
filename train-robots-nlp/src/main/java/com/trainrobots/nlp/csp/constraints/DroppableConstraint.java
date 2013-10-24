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
import com.trainrobots.nlp.csp.Model;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.WorldEntity;

public class DroppableConstraint extends EntityConstraint {

	@Override
	public Node toNode() {
		return new Node("droppable");
	}

	public List<WorldEntity> filter(Model model, List<WorldEntity> entities) {

		// Match the shape in the gripper.
		Shape shape = model.world().getShapeInGripper();
		if (shape != null) {
			for (WorldEntity grounding : entities) {
				if (grounding instanceof Shape) {
					if (grounding.equals(shape)) {
						List<WorldEntity> result = new ArrayList<WorldEntity>();
						result.add(shape);
						return result;
					}
				}
			}
		}

		// No match.
		throw new CoreException("Failed to match shape in gripper.");
	}
}