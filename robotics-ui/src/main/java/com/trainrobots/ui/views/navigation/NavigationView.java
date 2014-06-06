/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.data.DataService;
import com.trainrobots.ui.views.PaneView;

public class NavigationView extends PaneView {

	public NavigationView(DataService dataService, CommandService commandService) {
		super("Treebank");

		// Initiate.
		setSize(400, 550);
		setLayout(new BorderLayout());

		// Tree.
		NavigationTree navigationTree = new NavigationTree(dataService,
				commandService);

		// Scroll pane.
		add(new JScrollPane(navigationTree), BorderLayout.CENTER);
	}

	@Override
	public String paneType() {
		return "navigation";
	}
}