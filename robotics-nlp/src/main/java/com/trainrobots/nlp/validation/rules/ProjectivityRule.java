/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation.rules;

import java.util.function.Consumer;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.treebank.Command;

public class ProjectivityRule implements ValidationRule {

	@Override
	public String name() {
		return "projectivity";
	}

	@Override
	public void validate(Command command) {
		Losr losr = command.losr();
		if (losr == null) {
			return;
		}
		losr.visit(new Consumer<Losr>() {
			private int last;

			public void accept(Losr x) {
				if (x instanceof Terminal) {
					TextContext context = ((Terminal) x).context();
					if (context != null) {
						int start = context.start();
						int end = context.end();
						if (start > end) {
							throw new RoboticException(
									"Invalid tokenization at %s.", x);
						}
						if (start <= last) {
							throw new RoboticException(
									"Non-projectivity at %s.", x);
						}
						last = end;
					}
				}
			}
		});
	}
}