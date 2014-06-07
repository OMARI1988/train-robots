/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.command;

import java.util.Random;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Commands;
import com.trainrobots.treebank.Treebank;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.views.CommandView;
import com.trainrobots.ui.views.PaneView;

public class CommandService {

	private final TreebankService treebankService;
	private final WindowService windowService;
	private final Random random = new Random();

	private Command command;
	private boolean boundingBoxes;
	private boolean darkTheme;

	public CommandService(TreebankService treebankService,
			WindowService windowService) {
		this.treebankService = treebankService;
		this.windowService = windowService;
		Treebank treebank = treebankService.treebank();
		this.command = treebank.commands(treebank.scene(1)).get(0);
	}

	public Command command() {
		return command;
	}

	public void command(int commandId) {
		command(treebankService.treebank().command(commandId));
	}

	public void command(Command command) {

		// Status bar.
		this.command = command;
		windowService.status("Command %d", command.id());

		// Panes.
		for (PaneView pane : windowService.panes()) {
			if (pane instanceof CommandAware) {
				((CommandAware) pane).bindTo(command);
			}
		}
	}

	public void scene(int sceneId) {
		scene(treebankService.treebank().scene(sceneId));
	}

	public void scene(Scene scene) {
		Items<Command> commands = treebankService.treebank().commands(scene);
		if (commands == null || commands.count() == 0) {
			throw new RoboticException("Scene %d has not commands.", scene.id());
		}
		command(commands.get(0));
	}

	public boolean boundingBoxes() {
		return boundingBoxes;
	}

	public void boundingBoxes(boolean boundingBoxes) {
		this.boundingBoxes = boundingBoxes;
		CommandView commandView = windowService.pane(CommandView.class);
		if (commandView != null) {
			commandView.bindTo(command);
		}
	}

	public boolean darkTheme() {
		return darkTheme;
	}

	public void darkTheme(boolean darkTheme) {
		this.darkTheme = darkTheme;
		CommandView commandView = windowService.pane(CommandView.class);
		if (commandView != null) {
			commandView.bindTo(command);
		}
	}

	public void randomCommand() {
		Commands commands = treebankService.treebank().commands();
		Command command = null;
		do {
			command = commands.get(random.nextInt(commands.count()));
		} while (command.losr() == null);
		command(command);
	}
}