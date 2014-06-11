/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation.rules;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.treebank.Command;

public class StopWordRule implements ValidationRule {

	@Override
	public void validate(Command command) {
		Losr losr = command.losr();
		if (losr != null) {

			Items<Terminal> tokens = command.tokens();
			int size = tokens.count();
			boolean[] used = new boolean[size];

			losr.visit(x -> {
				if (x instanceof Terminal) {
					TextContext context = ((Terminal) x).context();
					if (context != null) {
						int start = context.start();
						int end = context.end();
						for (int i = start; i <= end; i++) {
							used[i - 1] = true;
						}
					}
				}
			});

			for (int i = 0; i < size; i++) {
				if (!used[i]) {
					validateStopWord(tokens.get(i));
				}
			}
		}
	}

	private void validateStopWord(Terminal token) {
		String text = token.context().text();
		if (text.equalsIgnoreCase("from")) {
			throw new RoboticException("'From' should not be a stop word.");
		}
	}
}