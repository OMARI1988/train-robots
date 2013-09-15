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

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.corpus.MarkType;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.processor.MoveValidator;

public class ParserTests {

	@Test
	public void shouldParse1() {
		Rcl rcl = Rcl.fromNode(Parser.parse("Pick up the blue pyramid."));
		assertEquals(rcl.toString(),
				"(event: (action: take) (entity: (color: blue) (type: prism)))");
	}

	@Test
	public void shouldParse2() {
		Rcl rcl = Rcl.fromNode(Parser
				.parse("Place the blue block on top of the red block."));
		assertEquals(
				rcl.toString(),
				"(event: (action: move) (entity: (color: blue) (type: cube)) (destination: (spatial-relation: (spatial-indicator: above) (entity: (color: red) (type: cube)))))");
	}

	@Test
	public void shouldParse3() {
		Node node = Parser.parse("white blocks");
		assertEquals(
				node,
				Node.fromString("(entity: (color: white) (type: cube) (number: plural))"));
	}

	@Test
	public void shouldParse4() {
		Node node = Parser.parse("the top left corner");
		assertEquals(
				node,
				Node.fromString("(entity: (spatial-indicator: front) (spatial-indicator: left) (type: corner))"));
	}

	@Test
	public void shouldParse5() {
		Node node = Parser.parse("move it");
		assertEquals(
				node,
				Node.fromString("(event: (action: move) (entity: (type: reference)))"));
	}

	@Test
	public void shouldParse6() {
		Node node = Parser.parse("the 3rd tile");
		assertEquals(node,
				Node.fromString("(entity: (ordinal: 3) (type: tile))"));
	}

	@Test
	public void shouldParse7() {
		Node node = Parser.parse("the yellow and purple blocks");
		assertEquals(
				node,
				Node.fromString("(entity: (color: yellow (conjunction: and (color: magenta))) (type: cube) (number: plural))"));
	}

	@Test
	public void shouldParse8() {
		Node node = Parser
				.parse("Pick the green pyramid and place on the red cube");
		assertEquals(
				node,
				Node.fromString("(sequence: (event: (action: take) (entity: (color: green) (type: prism))) (event: (action: move) (destination: (spatial-relation: (spatial-indicator: above) (entity: (color: red) (type: cube))))))"));
	}

	@Test
	public void shouldParse9() {
		Node node = Parser
				.parse("place the red prism on the top of blue sky block");
		assertEquals(
				node,
				Node.fromString("(event: (action: move) (entity: (color: red) (type: prism)) (destination: (spatial-relation: (spatial-indicator: above) (entity: (color: cyan) (type: cube)))))"));
	}

	@Test
	@Ignore
	public void shouldParseUnmarkedCommands() {

		// Parse.
		int total = 0;
		for (Command command : Corpus.getCommands()) {

			// Already marked?
			if (command.rcl != null || command.mark != MarkType.Unmarked) {
				continue;
			}

			// Parse.
			Rcl rcl;
			try {
				Node node = Parser.parse(command.text);
				rcl = Rcl.fromNode(node);
			} catch (Exception e) {
				continue;
			}
			try {
				MoveValidator.validate(command.sceneNumber, rcl);
				System.out
						.println("VALID: " + command.id + ": " + command.text);
			} catch (Exception e) {
				System.out.println(++total + ") " + command.id + ": "
						+ e.getMessage());
			}
		}
	}
}