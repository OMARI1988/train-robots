/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.lexicon;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;

public class LexicalKey {

	private LexicalKey() {
	}

	public static String key(Items<Terminal> tokens, TextContext context) {
		StringBuilder text = new StringBuilder();
		int start = context.start();
		int end = context.end();
		if (start == end) {
			return tokens.get(start - 1).context().text().toLowerCase();
		}
		for (int i = start; i <= end; i++) {
			if (text.length() > 0) {
				text.append(' ');
			}
			text.append(tokens.get(i - 1).context().text().toLowerCase());
		}
		return text.toString();
	}
}