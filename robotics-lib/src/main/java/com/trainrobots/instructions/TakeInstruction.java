/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.instructions;

import com.trainrobots.scenes.Position;

public class TakeInstruction implements Instruction {

	private final Position from;

	public TakeInstruction(Position from) {
		this.from = from;
	}

	public Position from() {
		return from;
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof TakeInstruction
				&& ((TakeInstruction) object).from.equals(from);
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append("Take: (");
		text.append(from);
		text.append(')');
		return text.toString();
	}
}