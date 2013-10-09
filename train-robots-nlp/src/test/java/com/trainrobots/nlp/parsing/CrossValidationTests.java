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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.chunker.Chunker;
import com.trainrobots.nlp.chunker.JitarChunker;
import com.trainrobots.nlp.chunker.Token;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.parser.grammar.Grammar;
import com.trainrobots.nlp.parser.lexicon.Lexicon;
import com.trainrobots.nlp.processor.MoveValidator;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class CrossValidationTests {

	@Test
	@Ignore
	public void shouldCrossValidateParser() {

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

			// Train chunker.
			JitarChunker chunker = new JitarChunker();
			chunker.train(trainList);

			// Lexicon.
			Lexicon lexicon = new Lexicon(trainList);
			Grammar grammar = new Grammar(trainList);

			// Evaluate.
			double p = evaluate(chunker, lexicon, grammar, testList);
			System.out.println("Fold " + (fold + 1) + ": " + df.format(p)
					+ " %");
			score += p;
		}

		// Average.
		System.out.println("-------------------");
		System.out.println("Average: " + df.format(0.1 * score) + " %");
	}

	private double evaluate(JitarChunker chunker, Lexicon lexicon,
			Grammar grammar, List<Command> testList) {

		int valid = 0;
		int count = 0;

		for (Command command : testList) {

			String text = command.text;
			List<Token> sequence = chunker.getSequence(text);
			List<Rcl> items = Chunker.getChunks(sequence);
			List<Node> chunks = new ArrayList<Node>();
			for (Rcl item : items) {
				chunks.add(item.toNode());
			}

			List<Node> tokens = Tokenizer.getTokens(text).children;

			WorldModel world = SceneManager.getScene(command.sceneNumber).before;
			Parser parser = new Parser(world, grammar, lexicon, chunks, tokens);

			try {
				Rcl rcl = parser.parse();
				if (rcl != null) {
					MoveValidator.validate(command.sceneNumber, rcl);
					valid++;
				}
			} catch (Exception e) {
			}
			count++;
		}

		return 100.0 * valid / (double) count;
	}
}