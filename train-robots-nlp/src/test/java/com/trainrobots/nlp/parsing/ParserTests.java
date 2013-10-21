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

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.corpus.MarkType;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.parser.GoldParser;
import com.trainrobots.nlp.parser.ParserResult;
import com.trainrobots.nlp.processor.MoveValidator;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldModel;

public class ParserTests {

	@Test
	@Ignore
	public void shouldParse1() {
		assertTrue(match(22380, true));
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
			// System.out.println("------------------------");
			// System.out.println(command.id);
			try {
				if (match(command.id)) {
					correct++;
				}
			} catch (Exception e) {
				System.out.println(command.id + ": " + e.getMessage());
				// e.printStackTrace(System.out);
			}
			total++;
		}

		// Gold.
		DecimalFormat df = new DecimalFormat("#.##");
		double p = 100 * correct / (double) total;
		System.out.println("Parsed: " + correct + " / " + total + " = "
				+ df.format(p) + " %");
	}

	@Test
	@Ignore
	public void shouldParseUnmarkedCommands() {

		// Parse.
		int count = 0;
		for (Command command : Corpus.getCommands()) {

			// Already marked?
			if (command.rcl != null || command.mark != MarkType.Unmarked
					|| command.enhancement != 0) {
				continue;
			}

			// Parse.
			WorldModel world = SceneManager.getScene(command.sceneNumber).before;
			ParserResult result;
			try {
				result = GoldParser.parse(world, command.text);
				if (result.rcl() == null) {
					// if (result.reason().equals("Validated duplicates.")) {
					// System.out.println(++count + ") " + command.id + ": "
					// + result.reason());
					// }
					continue;
				}
			} catch (Exception e) {
				continue;
			}
			try {
				MoveValidator.validate(command.sceneNumber, result.rcl());
				// System.out.println(++count + ") VALID: " + command.id + ": "
				// + command.text);
			} catch (Exception e) {
				System.out.println(++count + ") " + e.getMessage() + ": "
						+ command.id + ": " + command.text);
			}
		}
	}

	private boolean match(int id) {
		return match(id, false);
	}

	private boolean match(int id, boolean verbose) {

		// Command.
		Command command = Corpus.getCommand(id);

		// World.
		WorldModel world = SceneManager.getScene(command.sceneNumber).before;

		// Parse.
		ParserResult result = GoldParser.parse(world, command.text, verbose);

		// Validate.
		Node expected = command.rcl.toNode();
		if (result.rcl() != null && expected.equals(result.rcl().toNode())) {
			return true;
		}

		// No match.
		if (verbose) {
			System.out.println();
			System.out.println("Command  : " + command.text);
			System.out.println("Expected : " + expected);
			System.out.println("Parsed   : "
					+ (result != null ? result.toString() : "None."));
		}
		System.out.println(command.id
				+ ": "
				+ (result.reason() == null ? "Single tree mismatched." : result
						.reason()));
		return false;
	}
}