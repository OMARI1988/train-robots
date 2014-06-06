/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import com.trainrobots.collections.Items;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandService;

public class SceneNode extends TreeNode {

	private final CommandService commandService;
	private final Items<Command> commands;

	public SceneNode(Scene scene, CommandService commandService,
			Items<Command> commands) {
		super("Scene " + scene.id(), false);
		this.commandService = commandService;
		this.commands = commands;
	}

	@Override
	protected void createChildNodes() {
		for (Command command : commands) {
			add(new CommandNode(commandService, command));
		}
	}
}