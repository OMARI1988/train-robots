/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.dialog;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.losr.Terminal;

public class StarRule extends DialogRule {

	public StarRule(Items<Terminal> left, String right) {
		super(left, right);
	}

	@Override
	public FilterResult result(Items<Terminal> tokens) {

		// No match?
		int size = left.count() - 1;
		int inputSize = tokens.count();
		if (inputSize <= size) {
			return null;
		}

		// Match initial tokens.
		for (int i = 0; i < size; i++) {
			String leftText = left.get(i).context().text();
			String inputText = tokens.get(i).context().text();
			if (!leftText.equalsIgnoreCase(inputText)) {
				return null;
			}
		}

		// Match.
		// 
		Terminal[] reduction = new Terminal[inputSize - size];
		for (int i = 0; i < reduction.length; i++) {
			reduction[i] = tokens.get(size + i);
		}
		return new FilterResult(new ItemsArray(reduction));
	}
}