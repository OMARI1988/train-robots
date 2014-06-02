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

package com.trainrobots.nlp.parser;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Rcl;

public class Candidate {

	public final Rcl rcl;
	public double p = 1;

	public Candidate(Node node) {
		rcl = Rcl.fromNode(node);
		calculateP(node);
	}

	private void calculateP(Node node) {
		if (node.p != null) {
			p *= node.p;
		}
		if (node.children != null) {
			for (Node child : node.children) {
				calculateP(child);
			}
		}
	}
}