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

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.csp.constraints.CspConstraint;
import com.trainrobots.nlp.planning.Model;
import com.trainrobots.nlp.scenes.WorldEntity;

public class CspVariable {

	private final int id;
	private final List<CspConstraint> constraints = new ArrayList<CspConstraint>();

	public CspVariable(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}

	public void add(CspConstraint constraint) {
		constraints.add(constraint);
	}

	public Iterable<CspConstraint> constraints() {
		return constraints;
	}

	public List<WorldEntity> solve(Model model) {
		List<WorldEntity> entities = model.entities();
		for (CspConstraint constraint : constraints) {
			entities = constraint.filter(model, entities);
		}
		return entities;
	}

	public Node toNode() {
		Node node = new Node("x" + id + ":");
		for (CspConstraint constraint : constraints) {
			node.add(constraint.toNode());
		}
		return node;
	}

	@Override
	public String toString() {
		return toNode().toString();
	}

	public String format() {
		return toNode().format();
	}
}