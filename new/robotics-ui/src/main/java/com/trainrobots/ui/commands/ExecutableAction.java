/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.commands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ExecutableAction extends AbstractAction {

	private final Executable executable;

	public ExecutableAction(Executable executable) {
		this.executable = executable;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		executable.execute();
	}
}