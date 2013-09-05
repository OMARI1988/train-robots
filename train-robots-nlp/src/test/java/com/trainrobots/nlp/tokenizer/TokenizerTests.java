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

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.nlp.commands.Command;
import com.trainrobots.nlp.commands.Corpus;
import com.trainrobots.nlp.io.FileWriter;
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
				Tokenizer.getTokens("A B. C D."),
				Node.fromString("(Tokens (Text a) (Text b) (End .) (Text c) (Text d) (End .))"));
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
	public void shouldTokenizeNumbers() {
		assertEquals(Tokenizer.getTokens("4 plus 26"),
				Node.fromString("(Tokens (Number 4) (Text plus) (Number 26))"));
	}

	@Test
	public void shouldForceLowerCase() {
		assertEquals(Tokenizer.getTokens("4 PLUS 26"),
				Node.fromString("(Tokens (Number 4) (Text plus) (Number 26))"));
	}

	@Test
	@Ignore
	public void shouldTokenizeCorpus() {

		// Files.
		FileWriter writer = new FileWriter("c:/temp/tokens.txt");

		// Process.
		for (Command command : Corpus.getCommands()) {

			// Command.
			writer.writeLine("// Scene " + command.sceneNumber + ": "
					+ command.text);
			writer.writeLine();

			// Tokens.
			Node tokens = Tokenizer.getTokens(command.text);
			for (Node token : tokens.children) {
				writer.writeLine(token.toString());
			}
			writer.writeLine();
			writer.writeLine();
		}

		// Close.
		writer.close();
	}

	@Test
	@Ignore
	public void shouldBuildDictionary() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Command command : Corpus.getCommands()) {
			for (Node token : Tokenizer.getTokens(command.text).children) {
				if (token.tag.equals("Text")) {
					String key = token.getValue();
					Integer count = map.get(key);
					map.put(key, count != null ? count + 1 : 1);
				}
			}
		}
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}
}