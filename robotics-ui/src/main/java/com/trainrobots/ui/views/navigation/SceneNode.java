/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandService;

public class SceneNode extends TreeNode {

	private final Scene scene;
	private final CommandService commandService;
	private final Items<Command> commands;

	public SceneNode(Scene scene, CommandService commandService,
			Items<Command> commands) {
		super("Scene " + scene.id(), false);
		this.scene = scene;
		this.commandService = commandService;
		this.commands = commands;
	}

	public Scene scene() {
		return scene;
	}

	public CommandNode child(Command command) {
		int size = getChildCount();
		for (int i = 0; i < size; i++) {
			CommandNode commandNode = (CommandNode) getChildAt(i);
			if (commandNode.command() == command) {
				return commandNode;
			}
		}
		throw new RoboticException(
				"Failed to find child node for command ID '%d'.", command.id());
	}

	@Override
	protected void createChildNodes() {
		for (Command command : commands) {
			add(new CommandNode(commandService, command));
		}
	}
}