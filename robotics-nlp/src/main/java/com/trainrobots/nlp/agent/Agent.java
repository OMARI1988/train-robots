/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.agent;

import com.trainrobots.Log;
import com.trainrobots.collections.Items;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.nlp.grammar.Grammar;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.parser.ParserContext;
import com.trainrobots.nlp.tagger.Tagger;
import com.trainrobots.planner.Planner;
import com.trainrobots.scenes.Layout;
import com.trainrobots.tokenizer.Tokenizer;

public class Agent {

	private final Grammar grammar;
	private final Lexicon lexicon;
	private final Tagger tagger;

	public Agent(Grammar grammar, Lexicon lexicon, Tagger tagger) {
		this.grammar = grammar;
		this.lexicon = lexicon;
		this.tagger = tagger;
	}

	public String process(Layout layout, String message) {
		try {
			execute(layout, message);
			return "OK";
		} catch (Exception exception) {
			Log.error("Agent processing failed.", exception);
			String error = exception.getMessage();
			if (error == null) {
				error = exception.getClass().getSimpleName();
			}
			return error;
		}
	}

	private void execute(Layout layout, String message) {

		// Tokens.
		Items<Terminal> tokens = new Tokenizer(message).tokens();
		Items<Terminal> terminals = tagger.sequence(message).terminals(lexicon);

		// Parse.
		ParserContext context = new ParserContext(layout, tokens);
		Parser parser = new Parser(context, grammar, lexicon, false);
		Losr losr = parser.parse((Items) terminals);

		// Instruction.
		Planner planner = context.planner();
		Instruction instruction = planner.instruction(losr);

		// Execute.
		planner.simulator().execute(instruction);
	}
}