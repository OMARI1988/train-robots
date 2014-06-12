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
import com.trainrobots.losr.Destination;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Marker;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Source;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Type;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.services.validation.ValidationService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.visualization.themes.Theme;
import com.trainrobots.ui.visualization.themes.Themes;

public class MainMenu extends JMenuBar {

	public MainMenu(WindowService windowService, CommandService commandService,
			ValidationService validationService, TreebankService treebankService) {

		add(new Menu("File", 'F') {
			{
				addItem("Validate", "F5", validationService::validate);
				addItem("Save", "ctrl S", treebankService::save);
				addSeparator();
				addItem("Exit", "x", windowService::exit);
			}
		});

		add(new Menu("Edit", 'E') {
			{
				addItem("Change Node", "F2", commandService::change);
				addItem("Delete Node", "DELETE", commandService::delete);

				add(new Menu("New", 'N') {
					{
						add("Node", "ctrl N", Losr.class);
						addItem("Ellipsis", "ctrl alt E",
								commandService::addEllipsis);
						addSeparator();
						add("Action", "ctrl A", Action.class);
						add("Cardinal", "ctrl L", Cardinal.class);
						add("Color", "ctrl C", Color.class);
						add("Destination", "ctrl D", Destination.class);
						add("Entity", "ctrl E", Entity.class);
						add("Event", "ctrl V", Event.class);
						add("Indicator", "ctrl I", Indicator.class);
						add("Marker", "ctrl M", Marker.class);
						add("Relation", "ctrl R", Relation.class);
						add("Source", "ctrl alt S", Source.class);
						add("Spatial Relation", "ctrl P", SpatialRelation.class);
						add("Type", "ctrl T", Type.class);
						addSeparator();
						addItem("ID", "ctrl alt I", commandService::addId);
						addItem("Reference ID", "ctrl alt R",
								commandService::addReferenceId);
					}

					private <T extends Losr> void add(String name,
							String shortCutKey, Class<T> type) {
						addItem(name, shortCutKey,
								() -> commandService.add(type));
					}
				});

				addSeparator();
				addItem("Tag Command", "ctrl alt T", commandService::tag);
				addItem("Parse Command", "ctrl alt P", commandService::parse);
				addItem("Clear Command", null, commandService::clear);
			}
		});

		add(new Menu("View", 'V') {
			{
				addCheckedItem("Bounding Boxes", "ctrl B",
						commandService::boundingBoxes,
						commandService.boundingBoxes());
				addItem("Groundings", "ctrl G", commandService::ground);
				addSeparator();
				addItem("Random Command", "F1", commandService::randomCommand);
			}
		});

		add(new Menu("Window", 'W') {
			{
				addView("Treebank", "t", "navigation");
				addView("Command", "c", "command");
				addView("Scene", "s", "scene");
				addView("Comments", "m", "comments");
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