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

package com.trainrobots.nlp.parsing;

import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.chunker.Chunker;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.parser.TokenizedTree;

public class ParserTests {

	@Test
	public void shouldParse1() {
		assertTrue(match(84));
	}

	@Test
	public void shouldParse2() {
		assertTrue(match(14055));
	}

	@Test
	@Ignore
	public void shouldParse3() {
		assertTrue(match(28));
	}

	@Test
	@Ignore
	public void shouldParseCorpus() {

		// Parse.
		int correct = 0;
		int total = 0;
		for (Command command : Corpus.getCommands()) {
			if (command.rcl == null) {
				continue;
			}

			// Process.
			try {
				if (match(command.id)) {
					correct++;
					System.out.println(command.id + ": Valid.");
				} else {
					System.out.println(command.id + ": Misparsed.");
				}
			} catch (Exception e) {
				System.out.println(command.id + ": " + e.getMessage());
			}
			total++;
		}

		// Gold.
		DecimalFormat df = new DecimalFormat("#.##");
		double p = 100 * correct / (double) total;
		System.out.println("Parsed: " + correct + " / " + total + " = "
				+ df.format(p) + " %");
	}

	private static boolean match(int id) {

		// Command.
		Command command = Corpus.getCommand(id);

		// Parse.
		List<Node> chunks = Chunker.getChunks(command.rcl);
		Parser parser = new Parser(chunks);
		parser.parse();
		Node result = parser.result();

		// Validate.
		Rcl expected = TokenizedTree.getTree(command.rcl);
		return (expected.toNode().equals(result));
	}
}