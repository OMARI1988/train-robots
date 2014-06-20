/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui;

import com.trainrobots.Log;
import com.trainrobots.treebank.Treebank;

public class UiContext {

	private static Treebank treebank;

	private UiContext() {
	}

	public static Treebank treebank() {
		if (treebank == null) {
			Log.toConsole();
			treebank = new Treebank("../.data/treebank.zip");
		}
		return treebank;
	}
}