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
import com.trainrobots.nlp.losr.StopWords;
import com.trainrobots.treebank.Command;

public class StopWordRule implements ValidationRule {

	@Override
	public String name() {
		return "stop word";
	}

	@Override
	public void validate(Command command) {
		Losr losr = command.losr();
		if (losr != null) {
			StopWords.visit(
					command,
					x -> {
						String text = x.context().text();
						if (text.equalsIgnoreCase("board")) {
							throw new RoboticException(
									"'%s' should not be a stop word.", text);
						}
					});
		}
	}
}