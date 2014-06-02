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

import com.trainrobots.Robotics;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Ordinal;
import com.trainrobots.losr.Symbol;
import com.trainrobots.losr.Text;
import com.trainrobots.losr.Token;
import com.trainrobots.treebank.Command;

public class TokenizerTests {

	@Test
	public void shouldTokenizeText() {

		// Tokenize.
		Items<Token> tokens = new Tokenizer("pick up the red block").tokens();

		// Validate.
		assertThat(tokens.count(), is(5));
		assertThat(tokens.get(0), is(new Text("pick")));
		assertThat(tokens.get(1), is(new Text("up")));
		assertThat(tokens.get(2), is(new Text("the")));
		assertThat(tokens.get(3), is(new Text("red")));
		assertThat(tokens.get(4), is(new Text("block")));
	}

	@Test
	public void shouldTokenizeSymbols() {

		// Tokenize.
		Items<Token> tokens = new Tokenizer("you've, row.").tokens();

		// Validate.
		assertThat(tokens.count(), is(4));
		assertThat(tokens.get(0), is(new Text("you've")));
		assertThat(tokens.get(1), is(new Symbol(",")));
		assertThat(tokens.get(2), is(new Text("row")));
		assertThat(tokens.get(3), is(new Symbol(".")));
	}

	@Test
	public void shouldTokenizeIsolatedFullStop() {

		// Tokenize.
		Items<Token> tokens = new Tokenizer("you've, row .").tokens();

		// Validate.
		assertThat(tokens.count(), is(4));
		assertThat(tokens.get(0), is(new Text("you've")));
		assertThat(tokens.get(1), is(new Symbol(",")));
		assertThat(tokens.get(2), is(new Text("row")));
		assertThat(tokens.get(3), is(new Symbol(".")));
	}

	@Test
	public void shouldTokenizeTwoSentences() {

		// Tokenize.
		Items<Token> tokens = new Tokenizer("A B. C D!").tokens();

		// Validate.
		assertThat(tokens.count(), is(6));
		assertThat(tokens.get(0), is(new Text("A")));
		assertThat(tokens.get(1), is(new Text("B")));
		assertThat(tokens.get(2), is(new Symbol(".")));
		assertThat(tokens.get(3), is(new Text("C")));
		assertThat(tokens.get(4), is(new Text("D")));
		assertThat(tokens.get(5), is(new Symbol("!")));
	}

	@Test
	public void shouldTokenizeDashes() {

		// Tokenize.
		Items<Token> tokens = new Tokenizer("top of red-blue-red tower")
				.tokens();

		// Validate.
		assertThat(tokens.count(), is(8));
		assertThat(tokens.get(0), is(new Text("top")));
		assertThat(tokens.get(1), is(new Text("of")));
		assertThat(tokens.get(2), is(new Text("red")));
		assertThat(tokens.get(3), is(new Symbol("-")));
		assertThat(tokens.get(4), is(new Text("blue")));
		assertThat(tokens.get(5), is(new Symbol("-")));
		assertThat(tokens.get(6), is(new Text("red")));
		assertThat(tokens.get(7), is(new Text("tower")));
	}

	@Test
	public void shouldTokenizeForwardSlash() {

		// Tokenize.
		Items<Token> tokens = new Tokenizer("red/white column").tokens();

		// Validate.
		assertThat(tokens.count(), is(4));
		assertThat(tokens.get(0), is(new Text("red")));
		assertThat(tokens.get(1), is(new Symbol("/")));
		assertThat(tokens.get(2), is(new Text("white")));
		assertThat(tokens.get(3), is(new Text("column")));
	}

	@Test
	public void shouldTokenizeCardinals() {

		// Tokenize.
		Items<Token> tokens = new Tokenizer("4 plus 26").tokens();

		// Validate.
		assertThat(tokens.count(), is(3));
		assertThat(tokens.get(0), is(new Cardinal("4", 4)));
		assertThat(tokens.get(1), is(new Text("plus")));
		assertThat(tokens.get(2), is(new Cardinal("26", 26)));
	}

	@Test
	public void shouldTokenizeOrdinals() {

		// Tokenize.
		Items<Token> tokens = new Tokenizer("1st 2nd 3rd 4th 5th 6th").tokens();

		// Validate.
		assertThat(tokens.count(), is(6));
		assertThat(tokens.get(0), is(new Ordinal("1st", 1)));
		assertThat(tokens.get(1), is(new Ordinal("2nd", 2)));
		assertThat(tokens.get(2), is(new Ordinal("3rd", 3)));
		assertThat(tokens.get(3), is(new Ordinal("4th", 4)));
		assertThat(tokens.get(4), is(new Ordinal("5th", 5)));
		assertThat(tokens.get(5), is(new Ordinal("6th", 6)));
	}

	@Test
	public void shouldTokenizeTreebank() {
		int wordCount = 0;
		for (Command command : Robotics.system().commands()) {
			Items<Token> tokens = new Tokenizer(command.text()).tokens();
			for (Token token : tokens) {
				if (token instanceof Text || token instanceof Cardinal
						|| token instanceof Ordinal) {
					wordCount++;
				}
			}
		}
		assertThat(wordCount, is(125465));
	}
}