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

public class ProcessorTests {

	@Test
	public void shouldProcessCommand() {

		Command command = Corpus.getCommand(1663);
		MoveValidator.validate(command.sceneNumber, command.rcl);
	}

	@Test
	public void shouldProcessCorpus() {

		// Process.
		int error = 0;
		int correct = 0;
		int unmarked = 0;
		int notGold = 0;
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

			// Enhacement mismatch.
			if (command.enhancement != 0 && command.mark != MarkType.Unmarked) {
				System.out.println("C" + command.id
						+ ": Marked command with enhancement.");
			}

			// Command.
			if (command.mark != MarkType.Unmarked
					&& command.mark != MarkType.Accurate) {
				notGold++;
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
						+ command.text);
				System.out.println(e.getMessage());
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
		assertEquals(1663, correct);
		assertEquals(8644, size);

		// Gold.
		DecimalFormat df = new DecimalFormat("#.##");
		int reviewed = notGold + correct;
		int gold = correct;
		double p = 100 * gold / (double) reviewed;
		System.out.println("Gold: " + gold + " / " + reviewed + " = "
				+ df.format(p) + " %");

		// Progress.
		int estimatedSize = (int) Math.round(size * 0.01 * p);
		double p2 = 100 * correct / (double) estimatedSize;
		System.out.println("Annotated: " + correct + " / " + estimatedSize
				+ " (estimated) = " + df.format(p2) + " %");
	}
}