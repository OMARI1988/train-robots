/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.TestContext;
import com.trainrobots.nlp.validation.ValidationResult;
import com.trainrobots.nlp.validation.ValidationResults;
import com.trainrobots.nlp.validation.Validator;

public class ValidationTests {

	@Test
	public void shouldValidateTreebank() {
		Validator validator = new Validator();
		ValidationResults results = validator.validate(TestContext.treebank());
		for (ValidationResult result : results) {
			System.out.println(result.command().id() + " " + result.message());
		}
		assertThat(results.count(), is(0));
	}
}