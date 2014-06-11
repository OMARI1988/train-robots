/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation.rules;

import com.trainrobots.RoboticException;
import com.trainrobots.treebank.Command;

public class CommentRule implements ValidationRule {

	@Override
	public void validate(Command command) {
		if (command.losr() != null && command.comment() != null) {
			throw new RoboticException("Unexpected comment: %s",
					command.comment());
		}
	}
}