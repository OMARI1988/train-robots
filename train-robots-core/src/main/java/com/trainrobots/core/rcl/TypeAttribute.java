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

public class TypeAttribute extends Rcl {

	private Type type;

	public TypeAttribute(Type type) {
		this.type = type;
	}

	public TypeAttribute(Type type, int tokenStart, int tokenEnd) {
		this.type = type;
		this.tokenStart = tokenStart;
		this.tokenEnd = tokenEnd;
	}

	public static TypeAttribute fromString(String text) {
		return fromNode(Node.fromString(text));
	}

	public static TypeAttribute fromNode(Node node) {

		if (!node.hasTag("type:")) {
			throw new CoreException("Expected 'type:' not '" + node.tag + "'.");
		}

		Type type = Type.parse(node.getSingleLeaf());
		int[] tokens = getTokens(node);
		return tokens != null ? new TypeAttribute(type, tokens[0], tokens[1])
				: new TypeAttribute(type);
	}

	@Override
	public Node toNode() {
		Node node = new Node("type:");
		if (type != null) {
			node.add(type.toString());
		}
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

	public Type type() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public TypeAttribute cloneWithoutValue() {
		return new TypeAttribute(null, tokenStart, tokenEnd);
	}
}