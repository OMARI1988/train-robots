/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.data;

import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class DataService {

	private final Treebank treebank = new Treebank("../.data");
	private Command selectedCommand;

	public DataService() {
		selectedCommand = treebank.commands(treebank.scene(1)).get(0);
	}

	public Treebank treebank() {
		return treebank;
	}

	public Command selectedCommand() {
		return selectedCommand;
	}

	public void selectedCommand(Command selectedCommand) {
		this.selectedCommand = selectedCommand;
	}
}