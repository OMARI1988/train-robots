/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.command;

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

	public void select(int commandId) {
		select(dataService.treebank().command(commandId));
	}

	public void select(Command command) {

		// Select.
		dataService.selectedCommand(command);

		// Panes.
		for (PaneView pane : windowService.panes()) {
			if (pane instanceof CommandAware) {
				((CommandAware) pane).bindTo(command);
			}
		}
	}
}