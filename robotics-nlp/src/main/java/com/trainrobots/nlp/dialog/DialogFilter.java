/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.dialog;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Terminal;
import com.trainrobots.tokenizer.Tokenizer;

public class DialogFilter {

	private final List<DialogRule> rules = new ArrayList<DialogRule>();

	public DialogFilter() {
		addRule("I want you to *", "*");
		addRule("Hello", "Hello! I like to move shapes.");
	}

	public FilterResult filter(Items<Terminal> tokens) {
		for (DialogRule rule : rules) {
			FilterResult result = rule.result(tokens);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	private void addRule(String left, String right) {

		// Tokenize.
		Items<Terminal> tokens = new Tokenizer(left).tokens();

		// Create rule.
		DialogRule rule;
		if (right.equals("*")) {
			rule = new StarRule(tokens, right);
		} else {
			rule = new DialogRule(tokens, right);
		}

		// Add rule.
		rules.add(rule);
	}
}