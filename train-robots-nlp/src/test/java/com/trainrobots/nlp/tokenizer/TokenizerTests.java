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

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.nlp.trees.Node;

public class TokenizerTests {

	@Test
	public void shouldTokenizeWords() {
		assertEquals(
				Tokenizer.getTokens("pick up the red block"),
				Node.fromString("(tokens: (text: pick) (text: up) (text: the) (text: red) (text: block))"));
	}

	@Test
	public void shouldTokenizePunctuation() {
		assertEquals(
				Tokenizer.getTokens("you've, row."),
				Node.fromString("(tokens: (text: you've) (comma: ,) (text: row) (end: .))"));
	}

	@Test
	public void shouldTokenizeIsolatedFullStop() {
		assertEquals(
				Tokenizer.getTokens("you've, row ."),
				Node.fromString("(tokens: (text: you've) (comma: ,) (text: row) (end: .))"));
	}

	@Test
	public void shouldTokenizeTwoSentences() {
		assertEquals(
				Tokenizer.getTokens("A B. C D!"),
				Node.fromString("(tokens: (text: a) (text: b) (end: .) (text: c) (text: d) (end: !))"));
	}

	@Test
	public void shouldTokenizeDashes() {
		assertEquals(
				Tokenizer.getTokens("top of red-blue-red tower"),
				Node.fromString("(tokens: (text: top) (text: of) (text: red) (dash: -) (text: blue) (dash: -) (text: red) (text: tower))"));
	}

	@Test
	public void shouldTokenizeForwardSlash() {
		assertEquals(
				Tokenizer.getTokens("red/white column"),
				Node.fromString("(tokens: (text: red) (forward-slash: /) (text: white) (text: column))"));
	}

	@Test
	public void shouldTokenizeCardinals() {
		assertEquals(
				Tokenizer.getTokens("4 plus 26"),
				Node.fromString("(tokens: (cardinal: 4) (text: plus) (cardinal: 26))"));
	}

	@Test
	public void shouldTokenizeSuffixedOrdinals() {
		assertEquals(
				Tokenizer.getTokens("1st 2nd 3rd 4th 5th 6th"),
				Node.fromString("(tokens: (ordinal: 1) (ordinal: 2) (ordinal: 3) (ordinal: 4) (ordinal: 5) (ordinal: 6))"));
	}

	@Test
	public void shouldForceLowerCase() {
		assertEquals(
				Tokenizer.getTokens("4 PLUS 26"),
				Node.fromString("(tokens: (cardinal: 4) (text: plus) (cardinal: 26))"));
	}

	@Test
	public void souldTokenizeCorpus() {
		int count = 0;
		for (Command command : Corpus.getCommands()) {
			Node tokens = Tokenizer.getTokens(command.text);
			for (Node token : tokens.children) {
				if (token.hasTag("text:") || token.hasTag("cardinal:")
						|| token.hasTag("ordinal:")) {
					count++;
				}
			}
		}
		System.out.println("Word count: " + count);
		assertEquals(count, 99566);
	}
}