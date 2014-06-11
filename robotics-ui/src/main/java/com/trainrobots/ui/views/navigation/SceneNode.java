/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import java.awt.Color;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandService;

public class SceneNode extends TreeNode {

	private final Scene scene;
	private final CommandService commandService;
	private final Items<Command> commands;
	private Color color;

	public SceneNode(Scene scene, CommandService commandService,
			Items<Command> commands) {
		super(false);
		this.scene = scene;
		this.commandService = commandService;
		this.commands = commands;
		refresh();
	}

	public Scene scene() {
		return scene;
	}

	@Override
	public Color color() {
		return color;
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

	public void refresh() {

		// Commands.
		int valid = 0;
		int total = 0;
		int size = commands.count();
		CommandType summaryType = CommandType.NotAnnotated;
		for (int i = 0; i < size; i++) {
			CommandType commandType = CommandType.of(commands.get(i));
			if (commandType == CommandType.Valid) {
				valid++;
			}
			if (commandType != CommandType.Ignore) {
				total++;
			}
			if (commandType == CommandType.Warning) {
				summaryType = CommandType.Warning;
			}
		}

		// Name.
		StringBuilder text = new StringBuilder();
		text.append("Scene ");
		text.append(scene.id());
		text.append(" (");
		text.append(valid);
		text.append('/');
		text.append(total);
		text.append(')');
		name = text.toString();

		// Color.
		if (valid == total) {
			summaryType = CommandType.Valid;
		}
		color = summaryType.color();
	}

	@Override
	protected void createChildNodes() {
		int size = commands.count();
		for (int i = 0; i < size; i++) {
			add(new CommandNode(commandService, commands.get(i)));
		}
	}
}