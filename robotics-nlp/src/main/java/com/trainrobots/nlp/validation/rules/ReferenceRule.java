/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation.rules;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Losr;
import com.trainrobots.treebank.Command;

public class ReferenceRule implements ValidationRule {

	@Override
	public String name() {
		return "reference";
	}

	@Override
	public void validate(Command command) {

		boolean[] ids = new boolean[5];
		boolean[] referenceIds = new boolean[5];

		Losr losr = command.losr();
		if (losr != null) {
			losr.visit(x -> {
				if (x.id() != 0) {
					if (x.id() > 5) {
						throw new RoboticException("Invald ID: %d", x.id());
					}
					if (ids[x.id() - 1]) {
						throw new RoboticException("Duplicate ID: %d", x.id());
					}
					ids[x.id() - 1] = true;
				}
				if (x.referenceId() != 0) {
					referenceIds[x.referenceId() - 1] = true;
				}
			});
			for (int i = 0; i < ids.length; i++) {
				if (ids[i]) {
					if (i > 0 && !ids[i - 1]) {
						throw new RoboticException("Unexpected ID: %d", i + 1);
					}
					if (!referenceIds[i]) {
						throw new RoboticException("Unreferenced ID: %d", i + 1);
					}
				}
			}
		}
	}
}