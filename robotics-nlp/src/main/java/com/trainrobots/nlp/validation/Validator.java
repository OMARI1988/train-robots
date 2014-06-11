/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.nlp.validation.rules.AboveOrWithinRule;
import com.trainrobots.nlp.validation.rules.PlannerRule;
import com.trainrobots.nlp.validation.rules.StopWordRule;
import com.trainrobots.nlp.validation.rules.ValidationRule;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class Validator {

	private final List<ValidationRule> rules = new ArrayList<>();

	public Validator() {
		rules.add(new AboveOrWithinRule());
		rules.add(new PlannerRule());
		rules.add(new StopWordRule());
		// rules.add(new CommentRule());
	}

	public ValidationResults validate(Treebank treebank) {
		ValidationResults results = new ValidationResults();
		for (Command command : treebank.commands()) {
			for (ValidationRule rule : rules) {
				try {
					rule.validate(command);
				} catch (Exception exception) {
					results.add(command, exception.getMessage());
				}
			}
		}
		return results;
	}
}