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

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.io.FileWriter;
import com.trainrobots.nlp.commands.Command;
import com.trainrobots.nlp.commands.Corpus;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.trees.Node;

public class ParserTests {

	@Test
	public void shouldParse1() {
		Node node = Parser.parse("Pick up the blue pyramid.");
		assertEquals(
				node,
				Node.fromString("(Command (Action pick-up) (Object (Description definite) (Attribute blue) (Type prism)))"));
	}

	@Test
	public void shouldParse2() {
		Node node = Parser
				.parse("Place the blue block on top of the red block.");
		assertEquals(
				node,
				Node.fromString("(Command (Action place) (Object (Description definite) (Attribute blue) (Type cube)) (SpatialIndicator above (Object (Description definite) (Attribute red) (Type cube))))"));
	}

	@Test
	public void shouldParse3() {
		Node node = Parser.parse("white blocks");
		assertEquals(
				node,
				Node.fromString("(Object (Attribute white) (Type cube) (Number plural))"));
	}

	@Test
	public void shouldParse4() {
		Node node = Parser.parse("the top left corner");
		assertEquals(
				node,
				Node.fromString("(Object (Description definite) (Attribute top) (Attribute left) (Type corner))"));
	}

	@Test
	public void shouldParse5() {
		Node node = Parser.parse("move it");
		assertEquals(node,
				Node.fromString("(Command (Action move) (Anaphor it))"));
	}

	@Test
	public void shouldParse6() {
		Node node = Parser.parse("the 3rd tile");
		assertEquals(
				node,
				Node.fromString("(Object (Description definite) (Ordinal 3) (Type tile))"));
	}

	@Test
	public void shouldParse7() {
		Node node = Parser.parse("the yellow and purple blocks");
		assertEquals(
				node,
				Node.fromString("(Object (Description definite) (Attribute yellow (Conjunction and (Attribute magenta))) (Type cube) (Number plural))"));
	}

	@Test
	public void shouldParse8() {
		Node node = Parser.parse("Pick up and hold the yellow pyramid");
		assertEquals(
				node,
				Node.fromString("(Command (Action pick-up) (Conjunction and (Command (Action hold) (Object (Description definite) (Attribute yellow) (Type prism)))))"));
	}

	@Test
	@Ignore
	public void shouldParseCorpus() {

		// Files.
		FileWriter writer = new FileWriter("c:/temp/parse.txt");

		// Process.
		for (Command command : Corpus.getCommands()) {

			// Command.
			writer.writeLine("// Command " + command.id + ": " + command.text);
			writer.writeLine("// Scene " + command.sceneNumber);
			writer.writeLine();

			// Parse.
			Node parse = Parser.parse(command.text);
			writer.writeLine(parse.format());
			writer.writeLine();
			writer.writeLine();
		}

		// Close.
		writer.close();
	}
}