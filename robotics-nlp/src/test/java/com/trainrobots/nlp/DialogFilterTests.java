/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Terminal;
import com.trainrobots.nlp.dialog.FilterResult;
import com.trainrobots.tokenizer.Tokenizer;

public class DialogFilterTests {

	@Test
	public void shouldFilterDialog1() {
		verifyResponse("Hello", "Hello! I like to move shapes.");
		verifyResponse("HELLO", "Hello! I like to move shapes.");
	}

	@Test
	public void shouldFilterDialog2() {
		verifyReduction("I want you to pick up the red prism.",
				"pick up the red prism.");
	}

	private static void verifyResponse(String message, String expected) {

		// Result.
		Items<Terminal> tokens = new Tokenizer(message).tokens();
		FilterResult result = NlpContext.filter().filter(tokens);
		assertThat(result, is(not(nullValue())));
		assertThat(result.response(), is(expected));
		assertThat(result.reduction(), is(nullValue()));
	}

	private static void verifyReduction(String message, String expected) {

		// Result.
		Items<Terminal> tokens = new Tokenizer(message).tokens();
		FilterResult result = NlpContext.filter().filter(tokens);
		assertThat(result, is(not(nullValue())));
		assertThat(result.response(), is(nullValue()));

		// Reduction.
		Items<Terminal> expectedReduction = new Tokenizer(expected).tokens();
		Items<Terminal> actualReduction = result.reduction();
		int size = expectedReduction.count();
		assertThat(actualReduction.count(), is(size));
		for (int i = 0; i < size; i++) {
			assertThat(actualReduction.get(i), is(expectedReduction.get(i)));
		}
	}
}