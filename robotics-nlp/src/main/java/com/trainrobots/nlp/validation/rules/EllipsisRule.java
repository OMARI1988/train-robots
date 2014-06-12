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
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Terminal;
import com.trainrobots.treebank.Command;

public class EllipsisRule implements ValidationRule {

	@Override
	public String name() {
		return "ellipsis";
	}

	@Override
	public void validate(Command command) {
		Losr losr = command.losr();
		if (losr != null) {
			validate(losr);
		}
	}

	public void validate(Losr root) {
		root.visit(x -> {
			if (x instanceof Terminal) {
				if (x instanceof Relation) {
					Relation relation = (Relation) x;
					if (relation.context() == null) {
						throw new RoboticException("Elliptical relation.");
					}
				}
			}
		});
	}
}