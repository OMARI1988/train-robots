/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation;

import com.trainrobots.RoboticException;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.planner.Planner;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class Validator {

	private final AboveWithinRule aboveWithinRule = new AboveWithinRule();

	public ValidationResults validate(Treebank treebank) {
		ValidationResults results = new ValidationResults();
		for (Command command : treebank.commands()) {
			if (command.losr() != null) {
				try {
					validate(command);
					results.validCount(results.validCount() + 1);
				} catch (Exception exception) {
					results.add(command, exception.getMessage());
				}
			}
		}
		return results;
	}

	private void validate(Command command) {

		// Above/within.
		aboveWithinRule.validate(command.losr());

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