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

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.nodes.Node;

public class GssNode {

	private final int id;
	private final Node content;
	private final List<GssNode> parents = new ArrayList<GssNode>();

	public GssNode(int id, Node content) {
		this.id = id;
		this.content = content;
	}

	public int id() {
		return id;
	}

	public Node content() {
		return content;
	}

	public List<GssNode> parents() {
		return parents;
	}

	@Override
	public String toString() {
		return content.toString();
	}
}