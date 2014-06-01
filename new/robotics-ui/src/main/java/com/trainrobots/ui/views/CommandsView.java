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

import com.trainrobots.RoboticSystem;
import com.trainrobots.collections.Items;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.DataService;

public class CommandsView extends PaneView {

	public CommandsView(DataService dataService) {
		super("Commands");

		// Scene.
		Scene scene = dataService.selectedScene();
		RoboticSystem roboticSystem = dataService.system();
		Items<Command> commands = roboticSystem.commands().forScene(scene);

		// Initiate.
		setSize(300, 300);
		setLayout(new BorderLayout());

		// List.
		int size = commands.count();
		String[] items = new String[size];
		for (int i = 0; i < size; i++) {
			items[i] = commands.get(i).text();
		}
		JList list = new JList(items);
		add(list, BorderLayout.CENTER);
	}
}