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

public class CommandAction extends AbstractAction {

	private final Command command;

	public CommandAction(Command command) {
		this.command = command;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		command.execute();
	}
}