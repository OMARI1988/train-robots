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

import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandAware;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.views.PaneView;

public class NavigationView extends PaneView implements CommandAware {

	private final NavigationTree navigationTree;

	public NavigationView(TreebankService treebankService,
			CommandService commandService) {
		super("Treebank");

		// Initiate.
		setLayout(new BorderLayout());
		navigationTree = new NavigationTree(treebankService, commandService);
		add(new JScrollPane(navigationTree), BorderLayout.CENTER);
	}

	@Override
	public String paneType() {
		return "navigation";
	}

	@Override
	public void bindTo(Command command) {
		navigationTree.bindTo(command);
	}

	public void refresh() {
		navigationTree.repaint();
	}
}