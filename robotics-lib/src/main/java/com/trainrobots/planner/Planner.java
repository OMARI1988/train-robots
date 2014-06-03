/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.planner;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.RoboticException;
import com.trainrobots.instructions.DropInstruction;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.instructions.MoveInstruction;
import com.trainrobots.instructions.TakeInstruction;
import com.trainrobots.losr.Losr;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class Planner {

	public Instruction translate(Layout layout, Losr losr) {
		return new DropInstruction(); // TODO: FIX!!
	}

	public Instruction instruction(Layout before, Layout after) {

		// Removed.
		List<Shape> removed = new ArrayList<Shape>();
		for (Shape shape : before.shapes()) {
			if (after.shape(shape.position()) == null) {
				removed.add(shape);
			}
		}

		// Added.
		List<Shape> added = new ArrayList<Shape>();
		for (Shape shape : after.shapes()) {
			if (before.shape(shape.position()) == null) {
				added.add(shape);
			}
		}

		// Single shape.
		if (removed.size() != 1 || added.size() != 1) {
			throw new RoboticException(
					"Failed to infer instruction beacuse a single shape was not moved.");
		}
		Position p1 = removed.get(0).position();
		Position p2 = added.get(0).position();

		// Drop.
		if (p1.equals(before.gripper().position())) {
			return new DropInstruction();
		}

		// Take.
		if (p2.equals(after.gripper().position())) {
			return new TakeInstruction();
		}

		// Move.
		return new MoveInstruction(p1, p2);
	}
}