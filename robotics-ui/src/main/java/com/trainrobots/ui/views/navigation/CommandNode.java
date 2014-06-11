/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import java.awt.Color;

import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandService;

public class CommandNode extends TreeNode {

	private final CommandService commandService;
	private final Command command;

	public CommandNode(CommandService commandService, Command command) {
		super("Command " + command.id(), true);
		this.commandService = commandService;
		this.command = command;
	}

	public Command command() {
		return command;
	}

	@Override
	public Color color() {
		return CommandType.of(command).color();
	}

	@Override
	public void select() {
		commandService.command(command);
	}
}