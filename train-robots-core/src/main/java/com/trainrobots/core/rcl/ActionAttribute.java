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

package com.trainrobots.core.rcl;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;

public class ActionAttribute extends Rcl {

	private final Action action;

	public ActionAttribute(Action action) {
		this.action = action;
	}

	public static ActionAttribute fromString(String text) {
		return fromNode(Node.fromString(text));
	}

	public static ActionAttribute fromNode(Node node) {

		if (!node.hasTag("action:")) {
			throw new CoreException("Expected 'action:' not '" + node.tag
					+ "'.");
		}

		Action action = Action.valueOf(node.getValue());
		return new ActionAttribute(action);
	}

	@Override
	public Node toNode() {
		return new Node("action:", action.toString());
	}

	@Override
	public String generate() {
		throw new CoreException("NOT_IMPLEMENTED");
	}

	@Override
	public void accept(RclVisitor visitor) {
		throw new CoreException("NOT_IMPLEMENTED");
	}

	public Action action() {
		return action;
	}
}