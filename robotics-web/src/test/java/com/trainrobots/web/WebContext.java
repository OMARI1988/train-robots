/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.web;

import com.trainrobots.Log;
import com.trainrobots.treebank.Treebank;

public class WebContext {

	private static Treebank treebank;

	private WebContext() {
	}

	public static Treebank treebank() {
		if (treebank == null) {
			Log.toConsole();
			treebank = new Treebank("../.data/treebank.zip");
		}
		return treebank;
	}
}