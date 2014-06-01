/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.menus;

import javax.swing.JMenuBar;

import com.trainrobots.ui.services.RoboticService;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.RobotView;
import com.trainrobots.ui.views.SceneView;

public class MainMenu extends JMenuBar {

	public MainMenu(WindowService windowService, RoboticService roboticService) {

		add(new Menu("File", 'F') {
			{
				addItem("Exit", "x", () -> System.exit(0));
			}
		});

		add(new Menu("Window", 'W') {
			{
				addItem("Robot", "r", () -> windowService.show(new RobotView()));
				addItem("Scene", "s",
						() -> windowService.show(new SceneView(roboticService)));
			}
		});
	}
}