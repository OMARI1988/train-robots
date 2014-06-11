/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation.rules;

import com.trainrobots.RoboticException;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.losr.Losr;
import com.trainrobots.planner.Planner;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;

public class PlannerRule implements ValidationRule {

	@Override
	public void validate(Command command) {
		Losr losr = command.losr();
		if (losr != null) {

			// Planner.
			Scene scene = command.scene();
			Planner planner = new Planner(scene.before());

			// Validate.
			Instruction actual = planner.instruction(command.losr());
			if (!actual.equals(scene.instruction())) {
				throw new RoboticException("Instruction mismatch.");
			}
		}
	}
}