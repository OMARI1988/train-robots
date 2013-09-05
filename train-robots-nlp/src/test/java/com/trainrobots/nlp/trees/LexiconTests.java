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

package com.trainrobots.nlp.trees;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.nlp.commands.Command;
import com.trainrobots.nlp.commands.Corpus;
import com.trainrobots.nlp.io.FileWriter;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class LexiconTests {

	@Test
	public void shouldGetEntries() {
		assertEquals("(Color red)", Lexicon.get("red").toString());
		assertNull(Lexicon.get("foo"));
	}

	@Test
	@Ignore
	public void shouldTagCorpus() {

		// Files.
		FileWriter writer = new FileWriter("c:/temp/tags.txt");

		// Process.
		for (Command command : Corpus.getCommands()) {

			// Command.
			writer.writeLine("// Scene " + command.sceneNumber + ": "
					+ command.text);
			writer.writeLine();

			// Tokens.
			Node tokens = Tokenizer.getTokens(command.text);
			for (Node node : tokens.children) {
				if (node.tag.equals("Text")) {
					String value = node.getValue();
					Node entry = Lexicon.get(value);
					if (entry != null) {
						node = entry;
					} else {
						node.tag = "X";
					}
				}
				writer.writeLine(node.toString());
			}
			writer.writeLine();
			writer.writeLine();
		}

		// Close.
		writer.close();
	}
}