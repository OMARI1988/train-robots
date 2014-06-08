/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.tokenizer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.TestContext;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Ordinal;
import com.trainrobots.losr.Symbol;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.Text;
import com.trainrobots.losr.TextContext;
import com.trainrobots.treebank.Command;

public class TokenizerTests {

	@Test
	public void shouldTokenizeText() {

		// Tokenize.
		Items<Terminal> tokens = new Tokenizer("pick up the red block")
				.tokens();

		// Validate.
		verifyContext(tokens, 5);
		verifyText(tokens.get(0), "pick");
		verifyText(tokens.get(1), "up");
		verifyText(tokens.get(2), "the");
		verifyText(tokens.get(3), "red");
		verifyText(tokens.get(4), "block");
	}

	@Test
	public void shouldTokenizeSymbols() {

		// Tokenize.
		Items<Terminal> tokens = new Tokenizer("you've, row.").tokens();

		// Validate.
		verifyContext(tokens, 4);
		verifyText(tokens.get(0), "you've");
		verifySymbol(tokens.get(1), ',');
		verifyText(tokens.get(2), "row");
		verifySymbol(tokens.get(3), '.');
	}

	@Test
	public void shouldTokenizeIsolatedFullStop() {

		// Tokenize.
		Items<Terminal> tokens = new Tokenizer("you've, row .").tokens();

		// Validate.
		verifyContext(tokens, 4);
		verifyText(tokens.get(0), "you've");
		verifySymbol(tokens.get(1), ',');
		verifyText(tokens.get(2), "row");
		verifySymbol(tokens.get(3), '.');
	}

	@Test
	public void shouldTokenizeTwoSentences() {

		// Tokenize.
		Items<Terminal> tokens = new Tokenizer("A B. C D!").tokens();

		// Validate.
		verifyContext(tokens, 6);
		verifyText(tokens.get(0), "A");
		verifyText(tokens.get(1), "B");
		verifySymbol(tokens.get(2), '.');
		verifyText(tokens.get(3), "C");
		verifyText(tokens.get(4), "D");
		verifySymbol(tokens.get(5), '!');
	}

	@Test
	public void shouldTokenizeDashes() {

		// Tokenize.
		Items<Terminal> tokens = new Tokenizer("top of red-blue-red tower")
				.tokens();

		// Validate.
		verifyContext(tokens, 8);
		verifyText(tokens.get(0), "top");
		verifyText(tokens.get(1), "of");
		verifyText(tokens.get(2), "red");
		verifySymbol(tokens.get(3), '-');
		verifyText(tokens.get(4), "blue");
		verifySymbol(tokens.get(5), '-');
		verifyText(tokens.get(6), "red");
		verifyText(tokens.get(7), "tower");
	}

	@Test
	public void shouldTokenizeForwardSlash() {

		// Tokenize.
		Items<Terminal> tokens = new Tokenizer("red/white column").tokens();

		// Validate.
		verifyContext(tokens, 4);
		verifyText(tokens.get(0), "red");
		verifySymbol(tokens.get(1), '/');
		verifyText(tokens.get(2), "white");
		verifyText(tokens.get(3), "column");
	}

	@Test
	public void shouldTokenizeCardinals() {

		// Tokenize.
		Items<Terminal> tokens = new Tokenizer("4 plus 26").tokens();

		// Validate.
		verifyContext(tokens, 3);
		verifyCardinal(tokens.get(0), 4);
		verifyText(tokens.get(1), "plus");
		verifyCardinal(tokens.get(2), 26);
	}

	@Test
	public void shouldTokenizeOrdinals() {

		// Tokenize.
		Items<Terminal> tokens = new Tokenizer("1st 2nd 3rd 4th 5th 6th")
				.tokens();

		// Validate.
		verifyContext(tokens, 6);
		verifyOrdinal(tokens.get(0), "1st", 1);
		verifyOrdinal(tokens.get(1), "2nd", 2);
		verifyOrdinal(tokens.get(2), "3rd", 3);
		verifyOrdinal(tokens.get(3), "4th", 4);
		verifyOrdinal(tokens.get(4), "5th", 5);
		verifyOrdinal(tokens.get(5), "6th", 6);
	}

	@Test
	public void shouldTokenizeTreebank() {
		int wordCount = 0;
		for (Command command : TestContext.treebank().commands()) {
			Items<Terminal> tokens = new Tokenizer(command.text()).tokens();
			for (Terminal token : tokens) {
				if (token instanceof Text || token instanceof Cardinal
						|| token instanceof Ordinal) {
					wordCount++;
				}
			}
		}
		assertThat(wordCount, is(125465));
	}

	private static void verifyContext(Items<Terminal> tokens, int size) {
		assertThat(tokens.count(), is(size));
		for (int i = 0; i < size; i++) {
			TextContext context = tokens.get(i).context();
			assertThat(context.start(), is(i + 1));
			assertThat(context.end(), is(i + 1));
		}
	}

	private static void verifyText(Terminal token, String text) {
		assertThat(token, is(new Text(text)));
		assertThat(token.context().text(), is(text));
	}

	private static void verifySymbol(Terminal token, char ch) {
		assertThat(token, is(new Symbol(ch)));
		assertThat(token.context().text(), is(Character.toString(ch)));
	}

	private static void verifyCardinal(Terminal token, int value) {
		assertThat(token, is(new Cardinal(value)));
		assertThat(token.context().text(), is(Integer.toString(value)));
	}

	private static void verifyOrdinal(Terminal token, String text, int value) {
		assertThat(token, is(new Ordinal(value)));
		assertThat(token.context().text(), is(text));
	}
}