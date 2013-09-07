/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.nlp.tokenizer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.trainrobots.nlp.trees.Node;

public class TokenizerTests {

	@Test
	public void shouldTokenizeWords() {
		assertEquals(
				Tokenizer.getTokens("pick up the red block"),
				Node.fromString("(Tokens (Text pick) (Text up) (Text the) (Text red) (Text block))"));
	}

	@Test
	public void shouldTokenizePunctuation() {
		assertEquals(
				Tokenizer.getTokens("you've, row."),
				Node.fromString("(Tokens (Text you've) (Comma ,) (Text row) (End .))"));
	}

	@Test
	public void shouldTokenizeIsolatedFullStop() {
		assertEquals(
				Tokenizer.getTokens("you've, row ."),
				Node.fromString("(Tokens (Text you've) (Comma ,) (Text row) (End .))"));
	}

	@Test
	public void shouldTokenizeTwoSentences() {
		assertEquals(
				Tokenizer.getTokens("A B. C D!"),
				Node.fromString("(Tokens (Text a) (Text b) (End .) (Text c) (Text d) (End !))"));
	}

	@Test
	public void shouldTokenizeDashes() {
		assertEquals(
				Tokenizer.getTokens("top of red-blue-red tower"),
				Node.fromString("(Tokens (Text top) (Text of) (Text red) (Dash -) (Text blue) (Dash -) (Text red) (Text tower))"));
	}

	@Test
	public void shouldTokenizeForwardSlash() {
		assertEquals(
				Tokenizer.getTokens("red/white column"),
				Node.fromString("(Tokens (Text red) (ForwardSlash /) (Text white) (Text column))"));
	}

	@Test
	public void shouldTokenizeCardinals() {
		assertEquals(
				Tokenizer.getTokens("4 plus 26"),
				Node.fromString("(Tokens (Cardinal 4) (Text plus) (Cardinal 26))"));
	}

	@Test
	public void shouldForceLowerCase() {
		assertEquals(
				Tokenizer.getTokens("4 PLUS 26"),
				Node.fromString("(Tokens (Cardinal 4) (Text plus) (Cardinal 26))"));
	}
}