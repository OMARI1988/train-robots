/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.grammar;

import com.trainrobots.losr.Losr;

public class ProductionRule {

	private final String lhs;
	private final String[] rhs;
	private int frequency;

	public ProductionRule(Losr losr) {
		lhs = losr.name();
		int size = losr.count();
		rhs = new String[size];
		for (int i = 0; i < size; i++) {
			rhs[i] = losr.get(i).name();
		}
	}

	public String lhs() {
		return lhs;
	}

	public String get(int index) {
		return rhs[index];
	}

	public int count() {
		return rhs.length;
	}

	public int frequency() {
		return frequency;
	}

	public void frequency(int frequency) {
		this.frequency = frequency;
	}
}