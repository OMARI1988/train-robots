/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.treebank;

import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.treebank.Treebank;

public class TreebankService {

	private final Treebank treebank;
	private final Lexicon lexicon;

	public TreebankService() {
		this(new Treebank("../.data/treebank.zip"));
	}

	public TreebankService(Treebank treebank) {
		this.treebank = treebank;
		this.lexicon = new Lexicon(treebank);
	}

	public Treebank treebank() {
		return treebank;
	}

	public Lexicon lexicon() {
		return lexicon;
	}
}