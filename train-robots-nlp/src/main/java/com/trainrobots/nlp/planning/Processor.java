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

package com.trainrobots.nlp.planning;

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.DropMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.TakeMove;

public class Processor {

	private final WorldModel world;

	public Processor(WorldModel world) {
		this.world = world;
	}

	public void execute(List<Move> moves) {

		for (Move move : moves) {
			if (move instanceof TakeMove) {
				executeTakeMove((TakeMove) move);
			} else if (move instanceof DropMove) {
				executeDropMove((DropMove) move);
			} else {
				executeDirectMove((DirectMove) move);
			}
		}
	}

	private void executeTakeMove(TakeMove move) {

		if (world.getShapeInGripper() != null) {
			throw new CoreException("Gripper already occupied.");
		}

		Position position = move.from;
		Shape shape = world.getShape(position);

		if (shape == null) {
			throw new CoreException("Take move incorrectly specified.");
		}

		if (world.getShape(position.add(0, 0, 1)) != null) {
			throw new CoreException("Shape can't be moved.");
		}

		int ax = world.arm().x;
		int ay = world.arm().y;
		int az = world.arm().z;

		ax = shape.position().x;
		ay = shape.position().y;
		shape.setPosition(new Position(ax, ay, az));
		world.setArm(new Position(ax, ay, az));
		world.closeGripper();
		world.reindex();
	}

	private void executeDropMove(DropMove move) {

		Shape shape = world.getShapeInGripper();
		if (shape == null) {
			throw new CoreException("Gripper not occupied.");
		}

		Position p = world.getDropPosition(shape.position().x,
				shape.position().y);
		dropTo(p);
	}

	private void executeDirectMove(DirectMove move) {

		executeTakeMove(new TakeMove(move.from));
		dropTo(move.to);
	}

	private void dropTo(Position position) {

		Shape shape = world.getShapeInGripper();
		if (shape == null) {
			throw new CoreException("Gripper not occupied.");
		}

		shape.setPosition(position);
		world.setArm(new Position(position.x, position.y, world.arm().z));
		world.openGripper();
		world.reindex();
	}
}