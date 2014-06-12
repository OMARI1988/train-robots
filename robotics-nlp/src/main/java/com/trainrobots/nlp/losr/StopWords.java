/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.losr;

import java.util.function.Consumer;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.treebank.Command;

public class StopWords {

	private StopWords() {
	}

	public static void visit(Command command, Consumer<Terminal> visitor) {

		Items<Terminal> tokens = command.tokens();
		int size = tokens.count();
		boolean[] used = new boolean[size];

		command.losr().visit(x -> {
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
				visitor.accept(tokens.get(i));
			}
		}
	}
}