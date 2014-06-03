/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.instructions;

public class DropInstruction implements Instruction {

	@Override
	public boolean equals(Object object) {
		return object instanceof DropInstruction;
	}

	@Override
	public String toString() {
		return "Drop";
	}
}