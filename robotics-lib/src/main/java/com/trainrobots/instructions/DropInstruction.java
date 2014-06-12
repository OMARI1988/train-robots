/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.instructions;

import com.trainrobots.scenes.Position;

public class DropInstruction implements Instruction {

	private final Position to;

	public DropInstruction(Position to) {
		this.to = to;
	}

	public Position to() {
		return to;
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof DropInstruction
				&& ((DropInstruction) object).to.equals(to);
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append("Drop (");
		text.append(to);
		text.append(')');
		return text.toString();
	}
}