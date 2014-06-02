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

public abstract class Rcl {

	protected int tokenStart;
	protected int tokenEnd;

	public int tokenStart() {
		return tokenStart;
	}

	public void setTokenStart(int tokenStart) {
		this.tokenStart = tokenStart;
	}

	public int tokenEnd() {
		return tokenEnd;
	}

	public void setTokenEnd(int tokenEnd) {
		this.tokenEnd = tokenEnd;
	}

	public abstract Node toNode();

	public String generate() {
		return generate(null);
	}

	public abstract String generate(GenerationContext context);

	public static Rcl fromString(String text) {
		return fromNode(Node.fromString(text));
	}

	@Override
	public String toString() {
		return toNode().toString();
	}
	
	public void recurse(RclVisitor visitor) {
		accept(null, visitor);
	}

	protected abstract void accept(Rcl parent, RclVisitor visitor);

	public Rcl getElement(final int id) {

		class EntityVisitor extends RclVisitor {

			public Entity match;

			@Override
			public void visit(Rcl parent, Entity entity) {
				if (entity.id() != null && entity.id().equals(id)) {
					if (match != null) {
						throw new CoreException("Duplicate id " + id);
					}
					match = entity;
				}
			}
		}

		EntityVisitor visitor = new EntityVisitor();
		accept(null, visitor);
		return visitor.match;
	}

	public String format() {
		return toNode().format();
	}

	public Rcl clone() {
		return fromNode(toNode().clone());
	}

	public static Rcl fromNode(Node node) {

		if (node.hasTag("entity:")) {
			return Entity.fromNode(node);
		}

		if (node.hasTag("spatial-relation:")) {
			return SpatialRelation.fromNode(node);
		}

		if (node.hasTag("event:")) {
			return Event.fromNode(node);
		}

		if (node.hasTag("sequence:")) {
			return Sequence.fromNode(node);
		}

		if (node.hasTag("action:")) {
			return ActionAttribute.fromNode(node);
		}

		if (node.hasTag("color:")) {
			return ColorAttribute.fromNode(node);
		}

		if (node.hasTag("ordinal:")) {
			return OrdinalAttribute.fromNode(node);
		}

		if (node.hasTag("cardinal:")) {
			return CardinalAttribute.fromNode(node);
		}

		if (node.hasTag("type:")) {
			return TypeAttribute.fromNode(node);
		}

		if (node.hasTag("indicator:")) {
			return IndicatorAttribute.fromNode(node);
		}

		throw new CoreException("Unrecognized RCL element '" + node.tag + "'.");
	}

	protected void addAlignment(Node node) {
		if (tokenStart == 0) {
			return;
		}
		Node tokenNode = new Node("token:", Integer.toString(tokenStart));
		if (tokenEnd != tokenStart) {
			tokenNode.add(Integer.toString(tokenEnd));
		}
		node.add(tokenNode);
	}

	protected static int[] getTokens(Node node) {
		Node tokenNode = node.getChild("token:");
		if (tokenNode == null) {
			return null;
		}
		int tokenStart = Integer.parseInt(tokenNode.children.get(0).tag);
		int tokenEnd = tokenStart;
		if (tokenNode.children.size() >= 2) {
			tokenEnd = Integer.parseInt(tokenNode.children.get(1).tag);
		}
		return new int[] { tokenStart, tokenEnd };
	}
}