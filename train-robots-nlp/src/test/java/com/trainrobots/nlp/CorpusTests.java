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

package com.trainrobots.nlp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.nlp.commands.Command;
import com.trainrobots.nlp.commands.Corpus;
import com.trainrobots.nlp.io.FileReader;
import com.trainrobots.nlp.io.FileWriter;
import com.trainrobots.nlp.syntax.Chunker;
import com.trainrobots.nlp.syntax.Node;

import edu.berkeley.nlp.PCFGLA.CoarseToFineMaxRuleParser;
import edu.berkeley.nlp.PCFGLA.Grammar;
import edu.berkeley.nlp.PCFGLA.Lexicon;
import edu.berkeley.nlp.PCFGLA.ParserData;
import edu.berkeley.nlp.PCFGLA.TreeAnnotations;
import edu.berkeley.nlp.syntax.Tree;
import edu.berkeley.nlp.tokenizer.PTBLineLexer;
import edu.berkeley.nlp.util.Numberer;

public class CorpusTests {

	@Test
	public void shouldLoadCorpus() {
		assertEquals(5427, Corpus.getCommands().size());
	}

	@Test
	public void shouldBuildChunks() {

		// Commands.
		List<Command> commands = Corpus.getCommands();

		// File.
		FileReader reader = new FileReader("../data/parses.txt");
		FileWriter writer = new FileWriter("c:/temp/chunks.txt");

		// Process.
		String line;
		int index = 0;
		while ((line = reader.readLine()) != null) {

			// Parse tree.
			Node node = Node.fromString(line);

			// Command.
			writer.writeLine(commands.get(index).text);
			writer.writeLine(node.toString());

			// Chunks.
			for (String chunk : new Chunker().getChunks(node)) {
				writer.writeLine(chunk);
			}

			// Spacing.
			writer.writeLine();
			index++;
		}

		// Close.
		reader.close();
		writer.close();
	}

	@Test
	@Ignore
	public void shouldParseCorpus() throws IOException {

		// Load parser.
		System.out.println("Loading model...");
		String path = "C:/Program Files/BerkeleyParser/eng_sm6.gr";
		ParserData parserData = ParserData.Load(path);

		// Initiate.
		System.out.println("Initiating parser...");
		Grammar grammar = parserData.getGrammar();
		Lexicon lexicon = parserData.getLexicon();
		Numberer.setNumberers(parserData.getNumbs());
		CoarseToFineMaxRuleParser parser = new CoarseToFineMaxRuleParser(
				grammar, lexicon, 1, -1, false, false, false, false, false,
				true, true);
		System.out.println("Done!");

		// Tokenizer.
		PTBLineLexer tokenizer = new PTBLineLexer();

		// Commands.
		List<Command> commands = Corpus.getCommands();

		// Process.
		int n = 0;
		FileWriter writer = new FileWriter("c:/temp/parses.txt");
		for (Command command : commands) {
			try {

				// Text.
				String text = clean(command.text);

				// Tokenize.
				List<String> tokens = tokenizer.tokenizeLine(text);

				// Parse.
				Tree<String> tree = parser.getBestParse(tokens);

				// Unbinarize.
				tree = TreeAnnotations.unAnnotateTree(tree);

				// Write.
				writer.writeLine(tree.toString());

			} catch (Exception exception) {
				System.out.println("Failed to parse: [" + command.text + "]");
			}

			// Update.
			System.out.println(++n + " / " + commands.size());
		}
		writer.close();
	}

	private static String clean(String text) {
		return text.trim();
	}
}