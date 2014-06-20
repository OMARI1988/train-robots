/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.simulator;

import com.trainrobots.NotImplementedException;
import com.trainrobots.RoboticException;
import com.trainrobots.instructions.DropInstruction;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.instructions.MoveInstruction;
import com.trainrobots.instructions.TakeInstruction;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class Simulator {

	private final Layout layout;

	public Simulator(Layout layout) {
		this.layout = layout;
	}

	public Layout layout() {
		return layout;
	}

	public Position dropPosition(Position position) {
		int x = position.x();
		int y = position.y();
		for (int z = 0; z <= 7; z++) {
			Position result = new Position(x, y, z);
			if (layout.shape(result) == null) {
				return result;
			}
		}
		throw new RoboticException("Failed to find drop position.");
	}

	public void execute(Instruction instruction) {

		// Move.
		if (instruction instanceof MoveInstruction) {
			executeMove((MoveInstruction) instruction);
			return;
		}

		// Take.
		if (instruction instanceof TakeInstruction) {
			executeTake((TakeInstruction) instruction);
			return;
		}

		// Drop.
		if (instruction instanceof DropInstruction) {
			executeDrop((DropInstruction) instruction);
			return;
		}

		// Not supported.
		throw new RoboticException("%s is not supported.", instruction
				.getClass().getSimpleName());
	}

	private void executeMove(MoveInstruction instruction) {

		// Shape.
		Position from = instruction.from();
		Shape shape = layout.shape(from);
		if (shape == null) {
			throw new NotImplementedException("No shape to move.");
		}

		// Move gripper.
		Position to = instruction.to();
		layout.gripper().position(gripperPosition(to));

		// Move shape.
		layout.remove(shape);
		layout.add(shape.withPosition(to));
	}

	private void executeTake(TakeInstruction instruction) {

		// Shape.
		Position from = instruction.from();
		Shape shape = layout.shape(from);
		if (shape == null) {
			throw new NotImplementedException("No shape to take.");
		}

		// Move gripper.
		Position position = gripperPosition(from);
		layout.gripper().position(position);

		// Move shape.
		layout.remove(shape);
		layout.add(shape.withPosition(position));
	}

	private void executeDrop(DropInstruction instruction) {

		// Shape.
		Shape shape = layout.gripper().shape();
		if (shape == null) {
			throw new NotImplementedException("No shape to drop.");
		}

		// Move gripper.
		Position to = instruction.to();
		Position position = gripperPosition(to);
		layout.gripper().position(position);

		// Move shape.
		layout.remove(shape);
		layout.add(shape.withPosition(to));
	}

	private Position gripperPosition(Position p) {
		return new Position(p.x(), p.y(), 6);
	}
}