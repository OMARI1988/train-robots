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

package com.trainrobots.nlp.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.trainrobots.nlp.generation.Generator;
import com.trainrobots.nlp.trees.Node;

public class GeneratorTests {

	@Test
	public void shouldGeneratePronoun() {

		Node node = Node.fromString("(Object @X1)");
		assertEquals(Generator.generate(node), "it");
	}

	@Test
	public void shouldGenerateNoun() {

		Node node = Node.fromString("(Object (Class block))");
		assertEquals(Generator.generate(node), "block");
	}

	@Test
	public void shouldGenerateDefiniteNoun() {

		Node node = Node.fromString("(Object (Class block) (State definite))");
		assertEquals(Generator.generate(node), "the block");
	}

	@Test
	public void shouldGenerateNounWithAdjective() {

		Node node = Node.fromString("(Object (Class block) (Color red))");
		assertEquals(Generator.generate(node), "red block");
	}

	@Test
	public void shouldGenerateDefiniteNounWithAdjective() {

		Node node = Node
				.fromString("(Object (Class block) (State definite) (Color red))");
		assertEquals(Generator.generate(node), "the red block");
	}

	@Test
	public void shouldGenerateDefiniteNounWithTwoAdjectives() {

		Node node = Node
				.fromString("(Object (Class corner) (State definite) (Attribute left) (Attribute bottom))");
		assertEquals(Generator.generate(node), "the left bottom corner");
	}

	@Test
	public void shouldGenerateCommand() {

		Node node = Node
				.fromString("(Command (Action pick) (Arg (Object (Class pyramid) (State definite) (Color blue))))");

		assertEquals(Generator.generate(node), "pick the blue pyramid");
	}
}