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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.nlp.csp.constraints.Available;
import com.trainrobots.nlp.csp.constraints.PositionConstraint;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.DropMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.TakeMove;

public class EventNode extends ActionNode {

	private final Action action;
	private final EntityNode entity;
	private final PositionConstraint destination;

	public EventNode(Action action, EntityNode entity,
			PositionConstraint destination) {
		this.action = action;
		this.entity = entity;
		this.destination = destination;
	}

	@Override
	public Node toNode() {
		Node node = new Node("event:");
		node.add("action:", action.toString());
		node.add(entity.toNode());
		if (destination != null) {
			node.add(destination.toNode());
		}
		return node;
	}

	public List<Move> solve(Model model) {

		// Moves.
		ArrayList<Move> moves = new ArrayList<Move>();

		// Drop.
		if (action == Action.drop) {
			moves.add(solveDrop(model));
			return moves;
		}

		// Take.
		if (action == Action.take) {
			moves.add(solveTake(model));
			return moves;
		}

		// Move.
		if (action == Action.move) {
			moves.add(solveMove(model));
			return moves;
		}

		// No match.
		throw new CoreException("Event action '" + action + "' not recognized.");
	}

	private Move solveDrop(Model model) {

		Shape shape = model.world().getShapeInGripper();
		if (shape == null) {
			throw new CoreException("Not holding anything to drop.");
		}

		List<WorldEntity> groundings = entity.solve(model);
		Available.filterAvailable(model, groundings);
		if (groundings.size() != 1) {
			throw new CoreException("Expected one grounding.");
		}
		WorldEntity worldEntity = groundings.get(0);

		if (!worldEntity.equals(shape)) {
			throw new CoreException("Drop shape mismatch.");
		}

		if (destination != null) {
			Position position2 = destination.solve(model, shape.position());
			Position arm = model.world().arm();
			if (position2.x != arm.x || position2.y != arm.y) {
				throw new CoreException("Invalid drop position.");
			}
		}

		return new DropMove();
	}

	private Move solveTake(Model model) {

		if (destination != null) {
			throw new CoreException(
					"Take command must not have destination specified.");
		}

		List<WorldEntity> groundings = entity.solve(model);
		Available.filterAvailable(model, groundings);
		if (groundings.size() != 1) {
			throw new CoreException("Expected one grounding.");
		}
		WorldEntity worldEntity = groundings.get(0);
		return new TakeMove(getPosition(worldEntity));
	}

	private Move solveMove(Model model) {

		if (model.world().getShapeInGripper() != null) {
			throw new CoreException("Expected drop not move.");
		}

		List<WorldEntity> groundings = entity.solve(model);
		Available.filterAvailable(model, groundings);
		if (groundings.size() != 1) {
			throw new CoreException("Expected one grounding.");
		}
		Position position = getPosition(groundings.get(0));
		Position position2 = destination.solve(model, position);
		return new DirectMove(position, position2);
	}

	private static Position getPosition(WorldEntity entity) {

		if (entity instanceof Shape) {
			return ((Shape) entity).position();
		}

		if (entity instanceof Corner) {
			return ((Corner) entity).basePosition();
		}

		throw new CoreException("Failed to get position for " + entity);
	}
}