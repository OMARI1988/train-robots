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

public class CommandView extends PaneView {

	public CommandView() {
		super("Command 5414");

		// Initiate.
		setSize(400, 550);
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
}