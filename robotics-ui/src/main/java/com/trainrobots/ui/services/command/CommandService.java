/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.command;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.data.DataService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.views.PaneView;

public class CommandService {

	private final DataService dataService;
	private final WindowService windowService;

	public CommandService(DataService dataService, WindowService windowService) {
		this.dataService = dataService;
		this.windowService = windowService;
	}

	public void command(int commandId) {
		command(dataService.treebank().command(commandId));
	}

	public void command(Command command) {

		// Select.
		dataService.selectedCommand(command);

		// Status bar.
		windowService.status("Command %d", command.id());

		// Panes.
		for (PaneView pane : windowService.panes()) {
			if (pane instanceof CommandAware) {
				((CommandAware) pane).bindTo(command);
			}
		}
	}

	public void scene(int sceneId) {
		scene(dataService.treebank().scene(sceneId));
	}

	public void scene(Scene scene) {
		Items<Command> commands = dataService.treebank().commands(scene);
		if (commands == null || commands.count() == 0) {
			throw new RoboticException("Scene %d has not commands.", scene.id());
		}
		command(commands.get(0));
	}
}