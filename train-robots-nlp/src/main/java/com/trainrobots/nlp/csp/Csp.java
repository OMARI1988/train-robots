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

import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.planning.Model;
import com.trainrobots.nlp.scenes.WorldEntity;

public class Csp {

	private final CspVariable root;

	public Csp(CspVariable root) {
		this.root = root;
	}

	public List<WorldEntity> solve(Model model) {
		return root.solve(model);
	}

	public static Csp fromRcl(String text) {
		Rcl rcl = Rcl.fromString(text);
		return fromRcl(rcl, rcl);
	}

	public static Csp fromRcl(Rcl rcl, Rcl element) {
		return new CspConverter(rcl, element).csp();
	}

	public Node toNode() {
		return root.toNode();
	}

	@Override
	public String toString() {
		return root.toString();
	}

	public String format() {
		return root.format();
	}
}