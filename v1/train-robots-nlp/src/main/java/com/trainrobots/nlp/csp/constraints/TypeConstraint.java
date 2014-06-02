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
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.csp.Model;
import com.trainrobots.nlp.scenes.WorldEntity;

public class TypeConstraint extends EntityConstraint {

	private final Type type;

	public TypeConstraint(Type type) {
		this.type = type;
	}

	public Type type() {
		return type;
	}

	@Override
	public List<WorldEntity> filter(Model model, List<WorldEntity> entities) {
		List<WorldEntity> result = new ArrayList<WorldEntity>();
		for (WorldEntity entity : entities) {
			if (entity.type() == type) {
				result.add(entity);
			}
		}
		return result;
	}

	@Override
	public Node toNode() {
		return new Node("type:", type.toString());
	}
}