/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.lexicon;

import com.trainrobots.losr.Terminal;

public class LexicalEntry {

	private final Terminal terminal;
	private int count = 1;
	private double weight;

	public LexicalEntry(Terminal terminal) {
		this.terminal = terminal;
	}

	public Terminal terminal() {
		return terminal;
	}

	public int count() {
		return count;
	}

	public void count(int count) {
		this.count = count;
	}

	public double weight() {
		return weight;
	}

	public void weight(double weight) {
		this.weight = weight;
	}
}