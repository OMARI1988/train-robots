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

package com.trainrobots.nlp.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.NumberFormat;
import java.util.List;

import org.junit.Test;

import com.trainrobots.core.io.FileWriter;
import com.trainrobots.nlp.commands.Command;
import com.trainrobots.nlp.commands.Corpus;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.scenes.Color;
import com.trainrobots.nlp.scenes.Move;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Scene;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.ShapeType;
import com.trainrobots.nlp.trees.Node;

public class AgentTests {

	@Test
	public void shouldGetMove() {

		Node node = Parser
				.parse("place the yellow pyramid on top of the light grey brick");

		Scene scene = SceneManager.getScene(897);
		List<Move> moves = Agent.getMoves(scene.before, node);
		assertTrue(match(moves, scene.moves));
	}

	@Test
	public void shouldGetShape() {
		Node node = Node
				.fromString("(Object (Description definite) (Attribute yellow) (Type prism))");
		Scene scene = SceneManager.getScene(897);
		Shape shape = Agent.getShape(scene.before, node);
		assertEquals(shape, new Shape(Color.Yellow, ShapeType.Prism,
				new Position(3, 6, 2)));
	}

	@Test
	public void shouldProcessCorpus() {

		// File.
		FileWriter writer = new FileWriter("c:/temp/agent.html");
		String[] types = { "Failed", "Success", "Mismatch" };

		// Process.
		int valid = 0;
		int total = 0;
		int mismatch = 0;
		for (Command command : Corpus.getCommands()) {

			// Command.
			Scene scene = command.scene;
			String text = command.text;

			// Parse.
			Node node = Parser.parse(text);

			// Agent.
			int category = 0;
			try {
				List<Move> moves = Agent.getMoves(scene.before, node);
				if (match(moves, scene.moves)) {
					category = 1;
				} else if (moves != null) {
					category = 2;
				}
			} catch (Exception e) {
			}

			// Write.
			switch (category) {
			case 0:
				writer.writeLine("<pre>");
				break;
			case 1:
				writer.writeLine("<pre style='color:blue'>");
				break;
			case 2:
				writer.writeLine("<pre style='color:red'>");
				break;
			}
			writer.writeLine("// Command " + command.id + ": " + text);
			writer.writeLine("// Scene " + scene.number + ": "
					+ types[category]);
			writer.writeLine();
			writer.writeLine(node.format());
			writer.writeLine();
			writer.writeLine("</pre>");

			// Increment.
			if (category == 1) {
				valid++;
			} else if (category == 2) {
				mismatch++;
			}
			total++;
		}

		// Results.
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);

		double p = 100 * valid / (double) total;
		System.out.println("Score: " + valid + " / " + total + " = "
				+ nf.format(p) + "%");
		System.out.println("Mismatch: " + mismatch);

		assertEquals(180, valid);
		assertEquals(6205, total);
	}

	private boolean match(List<Move> moves1, List<Move> moves2) {
		int size1 = moves1 != null ? moves1.size() : 0;
		int size2 = moves2 != null ? moves2.size() : 0;
		if (size1 != size2) {
			return false;
		}
		for (int i = 0; i < size1; i++) {
			Move m1 = moves1.get(i);
			Move m2 = moves2.get(i);
			if (!m1.equals(m2)) {
				return false;
			}
		}
		return true;
	}
}