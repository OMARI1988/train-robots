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

package com.trainrobots.nlp.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.corpus.MarkType;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.Move;

public class ProcessorTests {

	@Test
	public void shouldFindTopOfStack() {
		WorldModel world = SceneManager.getScene(424).before;
		Rcl rcl = Rcl.fromNode(Parser
				.parse("Put the yellow pyramid on top of the grey tower."));
		List<Move> moves = Processor.getMoves(world, rcl);
		assertNotNull(moves);
	}

	@Test
	public void shouldProcessCorpus() {

		// Process.
		int error = 0;
		int total = 0;
		for (Command command : Corpus.getCommands()) {

			// RCL but not accurate?
			if (command.rcl != null && command.mark != MarkType.Accurate) {
				System.out
						.println("C"
								+ command.id
								+ ": RCL specified but command was not marked as accurate.");
			}

			// Accurate but no RCL?
			if (command.rcl == null && command.mark == MarkType.Accurate) {
				System.out
						.println("C"
								+ command.id
								+ ": Command marked as accurate but RCL was not specified.");
			}

			// Command.
			if (command.rcl == null || command.mark != MarkType.Accurate) {
				continue;
			}

			// Process.
			try {
				MoveValidator.validate(command.sceneNumber, command.rcl);
			} catch (Exception e) {
				System.out.println(++error + ") C" + command.id + ": "
						+ e.getMessage() + " " + command.rcl);
			}
			total++;
		}

		// Failed?
		if (error > 0) {
			fail(error + " processing error(s).");
		}

		// Count.
		assertEquals(11, total);
	}
}