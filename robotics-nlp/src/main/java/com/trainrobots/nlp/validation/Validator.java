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
import com.trainrobots.nlp.validation.rules.CommentRule;
import com.trainrobots.nlp.validation.rules.CurrentPositionRule;
import com.trainrobots.nlp.validation.rules.EllipsisRule;
import com.trainrobots.nlp.validation.rules.MarkerRule;
import com.trainrobots.nlp.validation.rules.PlannerRule;
import com.trainrobots.nlp.validation.rules.ProjectivityRule;
import com.trainrobots.nlp.validation.rules.ReferenceRule;
import com.trainrobots.nlp.validation.rules.StopWordRule;
import com.trainrobots.nlp.validation.rules.ValidationRule;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class Validator {

	private final List<ValidationRule> rules = new ArrayList<>();

	public Validator() {
		rules.add(new PlannerRule());
		rules.add(new ReferenceRule());
		rules.add(new AboveOrWithinRule());
		rules.add(new StopWordRule());
		rules.add(new CurrentPositionRule());
		rules.add(new EllipsisRule());
		rules.add(new ProjectivityRule());
		rules.add(new CommentRule());
		rules.add(new MarkerRule());
	}

	public ValidationResults validate(Treebank treebank) {
		ValidationResults results = new ValidationResults();
		for (Command command : treebank.commands()) {
			for (ValidationRule rule : rules) {

				// Ignore rule?
				String comment = command.comment();
				if (comment != null
						&& comment.contains("// ignore validation:")
						&& comment.contains(rule.name())) {
					continue;
				}

				// Validate.
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