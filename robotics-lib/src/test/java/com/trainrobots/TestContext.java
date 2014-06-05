/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import com.trainrobots.treebank.Treebank;

public class TestContext {

	private static Treebank treebank;

	private TestContext() {
	}

	public static Treebank treebank() {
		if (treebank == null) {
			treebank = new Treebank("../.data");
		}
		return treebank;
	}
}