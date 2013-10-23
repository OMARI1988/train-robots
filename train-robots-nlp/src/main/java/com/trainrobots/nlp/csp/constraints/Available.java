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

import java.util.List;

import com.trainrobots.nlp.csp.Model;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.Stack;
import com.trainrobots.nlp.scenes.WorldEntity;

public class Available {

	private Available() {
	}

	public static void filterAvailable(Model model, List<WorldEntity> groundings) {

		// Multiple groundings?
		if (groundings.size() > 1) {

			// Match the shape in the gripper.
			Shape shape = model.world().getShapeInGripper();
			if (shape != null) {
				for (WorldEntity grounding : groundings) {
					if (grounding instanceof Shape) {
						if (grounding.equals(shape)) {
							groundings.clear();
							groundings.add(shape);
							return;
						}
					}
				}
			}

			// Remove shapes that support others.
			for (int i = groundings.size() - 1; i >= 0; i--) {
				WorldEntity entity = groundings.get(i);
				if (entity instanceof Shape) {
					shape = (Shape) entity;
					if (model.world().getShape(shape.position().add(0, 0, 1)) != null) {
						groundings.remove(i);
					}
				}
			}

			// Exclude headless stacks.
			for (int i = groundings.size() - 1; i >= 0; i--) {
				if (groundings.get(i) instanceof Stack) {
					Stack stack = (Stack) groundings.get(i);
					if (!stack.includesHead()) {
						groundings.remove(i);
					}
				}
			}
		}
	}
}