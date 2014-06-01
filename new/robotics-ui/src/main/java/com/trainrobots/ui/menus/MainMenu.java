/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.menus;

import javax.swing.JMenuBar;

import com.trainrobots.ui.services.WindowService;

public class MainMenu extends JMenuBar {

	public MainMenu(WindowService windowService) {

		add(new Menu("File", 'F') {
			{
				addItem("Exit", "x", () -> System.exit(0));
			}
		});

		add(new Menu("Window", 'W') {
			{
				addItem("Robot", "r", () -> windowService.showRobotWindow());
			}
		});
	}
}