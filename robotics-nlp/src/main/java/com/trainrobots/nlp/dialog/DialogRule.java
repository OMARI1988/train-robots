/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.dialog;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Terminal;

public class DialogRule {

	protected final Items<Terminal> left;
	protected final String right;

	public DialogRule(Items<Terminal> left, String right) {
		this.left = left;
		this.right = right;
	}

	public Items<Terminal> left() {
		return left;
	}

	public String right() {
		return right;
	}

	public FilterResult result(Items<Terminal> tokens) {

		// No match?
		int size = left.count();
		if (tokens.count() != size) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			String leftText = left.get(i).context().text();
			String inputText = tokens.get(i).context().text();
			if (!leftText.equalsIgnoreCase(inputText)) {
				return null;
			}
		}

		// Match.
		return new FilterResult(right);
	}
}