/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandAware;
import com.trainrobots.ui.services.command.CommandService;

public class CommandView extends PaneView implements CommandAware {

	private final LosrView losrView;

	public CommandView(CommandService commandService) {
		super(title(commandService.command()));

		// View.
		setLayout(new BorderLayout());
		losrView = new LosrView(commandService);
		add(new JScrollPane(losrView), BorderLayout.CENTER);

		// Keys.
		bindKey("ESCAPE", losrView::clearSelection);
	}

	@Override
	public String paneType() {
		return "command";
	}

	@Override
	public boolean alwaysBehind() {
		return true;
	}

	@Override
	public void bindTo(Command command) {
		setTitle(title(command));
		losrView.bind();
	}

	public void redraw() {
		losrView.repaint();
	}

	private static String title(Command command) {
		return "Command " + command.id();
	}
}