/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.menus;

import javax.swing.JMenuBar;

import com.trainrobots.losr.Action;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Type;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.visualization.themes.Theme;
import com.trainrobots.ui.visualization.themes.Themes;

public class MainMenu extends JMenuBar {

	public MainMenu(WindowService windowService, CommandService commandService) {

		add(new Menu("File", 'F') {
			{
				addItem("Exit", "x", windowService::exit);
			}
		});

		add(new Menu("Edit", 'E') {
			{
				addLosr("New Action", "ctrl A", Action.class);
				addLosr("New Cardinal", "ctrl N", Cardinal.class);
				addLosr("New Color", "ctrl C", Color.class);
				addLosr("New Indicator", "ctrl I", Indicator.class);
				addLosr("New Relation", "ctrl R", Relation.class);
				addLosr("New Type", "ctrl T", Type.class);
			}

			private <T extends Losr> void addLosr(String name,
					String shortCutKey, Class<T> type) {
				addItem(name, shortCutKey, () -> commandService.addLosr(type));
			}
		});

		add(new Menu("View", 'V') {
			{
				addCheckedItem("Bounding Boxes", "ctrl B",
						commandService::boundingBoxes,
						commandService.boundingBoxes());
				addItem("Random Command", "F5", commandService::randomCommand);
			}
		});

		add(new Menu("Window", 'W') {
			{
				addView("Treebank", "t", "navigation");
				addView("Command", "c", "command");
				addView("Scene", "s", "scene");
				addView("Robot", "r", "robot");

				addSeparator();
				add(new Menu("Theme", 'h') {
					{
						addTheme("Simple", "s", Themes.Simple);
						addTheme("Detail", "d", Themes.Detail);
						addTheme("Dark", "k", Themes.Dark);
					}

					private void addTheme(String name, String shortCutKey,
							Theme theme) {
						addItem(name, shortCutKey,
								() -> commandService.theme(theme));
					}
				});
			}

			private void addView(String name, String shortCutKey,
					String paneType) {
				addItem(name, shortCutKey, () -> windowService.show(paneType));
			}
		});
	}
}