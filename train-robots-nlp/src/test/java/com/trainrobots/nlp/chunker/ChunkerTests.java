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

package com.trainrobots.nlp.chunker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;

public class ChunkerTests {

	@Test
	@Ignore
	public void shouldEvaluateChunker() {

		// Cross-validation.
		double score = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		for (int fold = 0; fold < 10; fold++) {

			// Data.
			List<Command> trainList = new ArrayList<Command>();
			List<Command> testList = new ArrayList<Command>();
			int i = 0;
			for (Command command : Corpus.getCommands()) {
				if (command.rcl == null) {
					continue;
				}
				if (i % 10 != fold) {
					trainList.add(command);
				} else {
					testList.add(command);
				}
				i++;
			}

			// Train.
			JitarChunker chunker = new JitarChunker();
			chunker.train(trainList);

			// Evaluate.
			double p = evaluate(false, chunker, testList);
			System.out.println("Fold " + (fold + 1) + ": " + df.format(p)
					+ " %");
			score += p;
		}

		// Average.
		System.out.println("-------------------");
		System.out.println("Average: " + df.format(0.1 * score) + " %");
	}

	private double evaluate(boolean log, Chunker chunker, List<Command> testList) {

		int countToken = 0;
		int validToken = 0;
		int failed = 0;
		for (Command command : testList) {

			List<Token> goldSequence = new GoldSequence(command).tokens();
			List<Token> predSequence = chunker.getSequence(command.text);

			int size = goldSequence.size();
			if (match(goldSequence, predSequence)) {
				countToken += size;
				validToken += size;
			} else {

				failed++;
				if (log) {
					System.out.println("// Command " + command.id);
				}

				for (int i = 0; i < size; i++) {
					Token gold = goldSequence.get(i);
					Token pred = predSequence.get(i);
					if (log) {
						System.out.print(gold.token + "\t" + gold.tag + "\t"
								+ pred.tag);
						if (!gold.tag.equals(pred.tag)) {
							System.out.print(" // ERROR");
						}
						System.out.println();
					}
					if (gold.tag.equals(pred.tag)) {
						validToken++;
					}
					countToken++;
				}
				if (log) {
					System.out.println();
				}
			}
		}

		// Score.
		System.out.println("Failed commands: " + failed + " / "
				+ testList.size());
		return 100 * validToken / (double) countToken;
	}

	private static boolean match(List<Token> goldSequence,
			List<Token> predSequence) {
		int size = goldSequence.size();
		if (predSequence.size() != size) {
			throw new CoreException("Tokenization mismatch.");
		}
		for (int i = 0; i < size; i++) {
			if (!goldSequence.get(i).tag.equals(predSequence.get(i).tag)) {
				return false;
			}
		}
		return true;
	}
}