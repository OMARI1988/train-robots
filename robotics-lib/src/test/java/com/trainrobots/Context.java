/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import com.trainrobots.treebank.Treebank;

public class Context {

	private static Treebank treebank;

	private Context() {
	}

	public static Treebank treebank() {
		if (treebank == null) {
			Log.toConsole();
			treebank = new Treebank("../.data/treebank.zip");
		}
		return treebank;
	}
}