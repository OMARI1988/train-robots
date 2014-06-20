/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp;

import com.trainrobots.Log;
import com.trainrobots.nlp.agent.Agent;
import com.trainrobots.nlp.grammar.Grammar;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.nlp.tagger.Tagger;
import com.trainrobots.treebank.Treebank;

public class NlpContext {

	private static final Treebank treebank;
	private static final Grammar grammar;
	private static final Lexicon lexicon;
	private static final Tagger tagger;
	private static final Agent agent;

	private NlpContext() {
	}

	static {
		Log.toConsole();
		treebank = new Treebank("../.data/treebank.zip");
		grammar = new Grammar(treebank);
		lexicon = new Lexicon(treebank);
		tagger = new Tagger(treebank, lexicon);
		agent = new Agent(grammar, lexicon, tagger);
	}

	public static Treebank treebank() {
		return treebank;
	}

	public static Grammar grammar() {
		return grammar;
	}

	public static Lexicon lexicon() {
		return lexicon;
	}

	public static Tagger tagger() {
		return tagger;
	}

	public static Agent agent() {
		return agent;
	}
}