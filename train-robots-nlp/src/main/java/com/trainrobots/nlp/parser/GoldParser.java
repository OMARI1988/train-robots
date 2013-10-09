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

package com.trainrobots.nlp.parser;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.chunker.Chunker;
import com.trainrobots.nlp.chunker.JitarChunker;
import com.trainrobots.nlp.chunker.Token;
import com.trainrobots.nlp.parser.grammar.Grammar;
import com.trainrobots.nlp.parser.lexicon.Lexicon;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class GoldParser {

	private static Lexicon lexicon;
	private static Grammar grammar;
	private static Chunker chunker;

	private GoldParser() {
	}

	public static Rcl parse(WorldModel world, String text) {
		return parse(world, text, false);
	}

	public static Rcl parse(WorldModel world, String text, boolean verbose) {

		if (lexicon == null) {
			System.out.println("Initiating gold parser...");

			System.out.println("Lexicon...");
			lexicon = Lexicon.goldLexicon();

			System.out.println("Grammar...");
			grammar = Grammar.goldGrammar();

			System.out.println("Chunker...");
			List<Command> commands = new ArrayList<Command>();
			for (Command command : Corpus.getCommands()) {
				if (command.rcl != null) {
					commands.add(command);
				}
			}
			chunker = new JitarChunker();
			chunker.train(commands);

			System.out.println("Parser ready!");
		}

		List<Token> sequence = chunker.getSequence(text);
		List<Rcl> items = Chunker.getChunks(sequence);
		List<Node> chunks = new ArrayList<Node>();
		for (Rcl item : items) {
			chunks.add(item.toNode());
		}

		List<Node> tokens = Tokenizer.getTokens(text).children;
		return new Parser(world, grammar, lexicon, chunks, tokens, verbose)
				.parse();
	}
}