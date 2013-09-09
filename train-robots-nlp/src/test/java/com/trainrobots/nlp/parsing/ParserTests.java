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

import org.junit.Test;

import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.trees.Node;

public class ParserTests {

	@Test
	public void shouldParse1() {
		Node node = Parser.parse("Pick up the blue pyramid.");
		assertEquals(
				node,
				Node.fromString("(event: (action: pick-up) (entity: (color: blue) (type: prism)))"));
	}

	@Test
	public void shouldParse2() {
		Node node = Parser
				.parse("Place the blue block on top of the red block.");
		assertEquals(
				node,
				Node.fromString("(event: (action: place) (entity: (color: blue) (type: cube)) (spatial-indicator: above (entity: (color: red) (type: cube))))"));
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
				Node.fromString("(entity: (location-attribute: top) (location-attribute: left) (type: corner))"));
	}

	@Test
	public void shouldParse5() {
		Node node = Parser.parse("move it");
		assertEquals(node,
				Node.fromString("(event: (action: move) (anaphor: it))"));
	}

	@Test
	public void shouldParse6() {
		Node node = Parser.parse("the 3rd tile");
		assertEquals(
				node,
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
		Node node = Parser.parse("Pick up and hold the yellow pyramid");
		assertEquals(
				node,
				Node.fromString("(event: (action: pick-up) (conjunction: and (event: (action: hold) (entity: (color: yellow) (type: prism)))))"));
	}
}