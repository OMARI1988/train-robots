/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.menus;

import javax.swing.JMenuBar;

import com.trainrobots.ui.services.window.WindowService;

public class MainMenu extends JMenuBar {

	public MainMenu(WindowService windowService) {

		add(new Menu("File", 'F') {
			{
				addItem("Exit", "x", windowService::exit);
			}
		});

		add(new Menu("Window", 'W') {
			{
				addView("Command", "c", "command");
				addView("Commands", "d", "commands");
				addView("Scene", "s", "scene");
				addView("Scenes", "n", "scenes");
				addSeparator();
				addView("Robot", "r", "robot");
			}

			private void addView(String name, String shortCutKey,
					String paneType) {
				addItem(name, shortCutKey, () -> windowService.show(paneType));
			}
		});
	}
}