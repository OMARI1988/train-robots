/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.instructions;

import com.trainrobots.scenes.Position;

public class MoveInstruction implements Instruction {

	private final Position from;
	private final Position to;

	public MoveInstruction(Position from, Position to) {
		this.from = from;
		this.to = to;
	}

	public Position from() {
		return from;
	}

	public Position to() {
		return to;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof MoveInstruction) {
			MoveInstruction move = (MoveInstruction) object;
			return move.from.equals(from) && move.to.equals(to);
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append("Move (");
		text.append(from);
		text.append(") -> (");
		text.append(to);
		text.append(")");
		return text.toString();
	}
}