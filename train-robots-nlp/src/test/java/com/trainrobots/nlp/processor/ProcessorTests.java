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
import static org.junit.Assert.fail;

import java.text.DecimalFormat;

import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.corpus.MarkType;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.nlp.parser.Parser;

public class ProcessorTests {

	@Test
	public void shouldPlaceBlockOntoStackInCorner() {
		Event event = Event.fromNode(Parser
				.parse("move the yellow pyramid on the top left corner"));
		MoveValidator.validate(822, event);
	}

	@Test
	public void shouldTakeBlockFromBoard() {
		Event event = Event
				.fromString("(event: (action: take) (entity: (color: blue) (type: cube) (spatial-relation: (spatial-indicator: above) (entity: (type: board)))))");
		MoveValidator.validate(337, event);
	}

	@Test
	public void shouldProcessSequence() {

		Sequence sequence = Sequence
				.fromString("(sequence: (event: (action: take) (entity: (id: 1) (color: red) (type: cube))) (event: (action: drop) (entity: (type: reference) (reference-id: 1)) (destination: (spatial-relation: (spatial-indicator: above) (entity: (color: yellow) (type: cube))))))");
		MoveValidator.validate(708, sequence);
	}

	@Test
	public void shouldProcessCorpus() {

		// Process.
		int error = 0;
		int correct = 0;
		int unmarked = 0;
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
			if (command.mark != MarkType.Unmarked
					&& command.mark != MarkType.Accurate) {
				continue;
			}
			if (command.rcl == null) {
				unmarked++;
				continue;
			}

			// Process.
			try {
				MoveValidator.validate(command.sceneNumber, command.rcl);
			} catch (Exception e) {
				System.out.println(++error + ") C" + command.id + ": "
						+ e.getMessage());
				System.out.println(command.rcl.format());
			}
			correct++;
		}

		// Failed?
		if (error > 0) {
			fail(error + " processing error(s).");
		}

		// Count.
		int size = correct + unmarked;
		assertEquals(1176, correct);
		assertEquals(8805, size);

		// Stats.
		DecimalFormat df = new DecimalFormat("#.##");
		double p = 100 * correct / (double) size;
		System.out.println("RCL correct: " + correct + " / " + size + " = "
				+ df.format(p) + " %");
	}
}