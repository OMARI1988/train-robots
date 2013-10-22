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

public class Csp {

	private final List<CspVariable> variables = new ArrayList<CspVariable>();

	public CspVariable add() {
		int id = variables.size() + 1;
		CspVariable variable = new CspVariable(id);
		variables.add(variable);
		return variable;
	}

	public Node toNode() {
		Node node = new Node("csp");
		for (CspVariable variable : variables) {
			node.add(variable.toNode());
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