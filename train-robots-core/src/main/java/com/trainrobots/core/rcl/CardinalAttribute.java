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
import com.trainrobots.core.rcl.generation.GenerationContext;

public class CardinalAttribute extends Rcl {

	private Integer cardinal;

	public CardinalAttribute(Integer cardinal) {
		this.cardinal = cardinal;
	}

	public CardinalAttribute(Integer cardinal, int tokenStart, int tokenEnd) {
		this.cardinal = cardinal;
		this.tokenStart = tokenStart;
		this.tokenEnd = tokenEnd;
	}

	public static CardinalAttribute fromString(String text) {
		return fromNode(Node.fromString(text));
	}

	public static CardinalAttribute fromNode(Node node) {

		if (!node.hasTag("cardinal:")) {
			throw new CoreException("Expected 'cardinal:' not '" + node.tag
					+ "'.");
		}

		int cardinal = Integer.parseInt(node.getSingleLeaf());
		int[] tokens = getTokens(node);
		return tokens != null ? new CardinalAttribute(cardinal, tokens[0],
				tokens[1]) : new CardinalAttribute(cardinal);
	}

	@Override
	public Node toNode() {
		Node node = new Node("cardinal:");
		if (cardinal != null) {
			node.add(cardinal.toString());
		}
		addAlignment(node);
		return node;
	}

	public Node toNodeWithoutValue() {
		Node node = new Node("cardinal:");
		addAlignment(node);
		return node;
	}

	@Override
	public String generate(GenerationContext context) {
		throw new CoreException("NOT_IMPLEMENTED");
	}

	@Override
	public void accept(Rcl parent, RclVisitor visitor) {
		visitor.visit(parent, this);
	}

	public int cardinal() {
		return cardinal;
	}

	public void setCardinal(Integer cardinal) {
		this.cardinal = cardinal;
	}
}