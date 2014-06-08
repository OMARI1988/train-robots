/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.command;

import java.util.Random;
import java.util.function.Consumer;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Losr;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Commands;
import com.trainrobots.treebank.Treebank;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.views.PaneView;
import com.trainrobots.ui.views.command.CommandView;
import com.trainrobots.ui.visualization.themes.Theme;
import com.trainrobots.ui.visualization.themes.Themes;

public class CommandService {

	private final TreebankService treebankService;
	private final WindowService windowService;
	private final Random random = new Random();

	private Command command;
	private boolean boundingBoxes;
	private Theme theme = Themes.Detail;

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

		// Same command?
		if (command == this.command) {
			return;
		}

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
		execute(v -> v.redraw());
	}

	public Theme theme() {
		return theme;
	}

	public void theme(Theme theme) {
		this.theme = theme;
		execute(v -> v.bindTo(command));
	}

	public void randomCommand() {
		Commands commands = treebankService.treebank().commands();
		Command command = null;
		do {
			command = commands.get(random.nextInt(commands.count()));
		} while (command.losr() == null);
		command(command);
	}

	public <T extends Losr> void addLosr(Class<T> type) {
		execute("Add " + type.getSimpleName().toLowerCase(), v -> v.editor()
				.addLosr(type));
	}

	private void execute(Consumer<CommandView> action) {
		execute(null, action);
	}

	private void execute(String status, Consumer<CommandView> action) {
		try {
			CommandView commandView = windowService.pane(CommandView.class);
			if (commandView != null) {
				action.accept(commandView);
			}
			if (status != null) {
				windowService.status(status);
			}
		} catch (Exception exception) {
			if (exception.getMessage() == null) {
				Log.error("Failed to execute UI action.", exception);
			} else {
				windowService.error(exception.getMessage());
			}
		}
	}
}