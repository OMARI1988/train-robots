/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.treebank;

import com.trainrobots.treebank.Treebank;

public class TreebankService {

	private final Treebank treebank = new Treebank("../.data");

	public Treebank treebank() {
		return treebank;
	}
}