/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandAware;
import com.trainrobots.ui.services.data.DataService;

public class CommandView extends PaneView implements CommandAware {

	public CommandView(DataService dataService) {
		this(dataService.selectedCommand());
	}

	public CommandView(Command command) {
		super(title(command));

		// Initiate.
		setLayout(new BorderLayout());

		// Editor.
		JTextArea editor = new JTextArea();
		editor.setFont(new Font("Consolas", Font.PLAIN, 12));
		editor.setTabSize(2);

		// Scroll pane.
		add(new JScrollPane(editor), BorderLayout.CENTER);
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
	}

	private static String title(Command command) {
		return "Command " + command.id();
	}
}