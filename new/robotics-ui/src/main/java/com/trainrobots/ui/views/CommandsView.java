/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;

import javax.swing.JList;

public class CommandsView extends PaneView {

	public CommandsView() {
		super("Commands");

		// Initiate.
		setSize(300, 300);
		setLayout(new BorderLayout());

		// List.
		JList list = new JList(
				new String[] { "AAAAA", "BBBBB", "CCCC", "DDDDD" });
		add(list, BorderLayout.CENTER);
	}
}