/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Terminal;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.nlp.tagger.Sequence;
import com.trainrobots.nlp.tagger.Tagger;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class TaggerTests {

	@Test
	@Ignore
	public void shouldTagTreebank() {

		// Lexicon.
		Treebank treebank = NlpContext.treebank();
		Lexicon lexicon = NlpContext.lexicon();

		// Tagger.
		Tagger tagger = new Tagger(treebank, lexicon);

		// Test.
		for (Command command : treebank.commands()) {
			Items<Terminal> terminals = tagger.terminals(command);
			System.out.println();
			System.out.println(command.text());
			System.out.println(terminals);
		}
	}

	@Test
	@Ignore
	public void shouldEvaluateTagger() {

		// Cross-validation.
		double score = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		for (int fold = 0; fold < 10; fold++) {

			// Data.
			List<Command> trainList = new ArrayList<Command>();
			List<Command> testList = new ArrayList<Command>();
			int i = 0;
			for (Command command : NlpContext.treebank().commands()) {
				if (command.losr() == null) {
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
			Tagger tagger = new Tagger(trainList);

			// Evaluate.
			double p = evaluate(false, tagger, testList);
			System.out.println("Fold " + (fold + 1) + ": " + df.format(p)
					+ " %");
			score += p;
		}

		// Average.
		System.out.println("-------------------");
		System.out.println("Average: " + df.format(0.1 * score) + " %");
	}

	private double evaluate(boolean log, Tagger tagger, List<Command> testList) {

		int countToken = 0;
		int validToken = 0;
		int failed = 0;
		for (Command command : testList) {

			Sequence gold = new Sequence(command);
			Sequence predicted = tagger.sequence(command.text());

			int size = gold.count();
			if (match(gold, predicted)) {
				countToken += size;
				validToken += size;
			} else {

				failed++;
				if (log) {
					System.out.println("// Command " + command.id());
				}

				for (int i = 0; i < size; i++) {
					if (log) {
						System.out.print(gold.text(i) + "\t" + gold.tag(i)
								+ "\t" + predicted.tag(i));
						if (!gold.tag(i).equals(predicted.tag(i))) {
							System.out.print(" // ERROR");
						}
						System.out.println();
					}
					if (gold.tag(i).equals(predicted.tag(i))) {
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

	private static boolean match(Sequence gold, Sequence predicted) {
		int size = gold.count();
		if (predicted.count() != size) {
			throw new RoboticException("Tokenization mismatch.");
		}
		for (int i = 0; i < size; i++) {
			if (!gold.tag(i).equals(predicted.tag(i))) {
				return false;
			}
		}
		return true;
	}
}