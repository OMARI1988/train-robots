/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.window;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.ui.Container;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.data.DataService;
import com.trainrobots.ui.views.CommandView;
import com.trainrobots.ui.views.MainWindow;
import com.trainrobots.ui.views.PaneView;
import com.trainrobots.ui.views.RobotView;
import com.trainrobots.ui.views.SceneView;
import com.trainrobots.ui.views.navigation.NavigationView;

public class WindowService {

	private static final String UI_XML_FILE = "../.data/ui.xml";
	private final Map<String, Class> paneTypes = new HashMap<>();
	private final Container container;
	private MainWindow mainWindow;
	private int windowCount;

	public WindowService(Container container) {

		// Initiate.
		this.container = container;

		// Panes.
		registerPane("robot", RobotView.class);
		registerPane("scene", SceneView.class);
		registerPane("navigation", NavigationView.class);
		registerPane("command", CommandView.class);
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void showMainWindow() {

		// Restore layout.
		try {

			// Load panes.
			PaneBuilder paneBuilder = (paneType, x, y, width, height) -> show(
					paneType, x, y, new Dimension(width, height));
			SettingsReader reader = new SettingsReader(paneBuilder);
			reader.read(UI_XML_FILE);

			// Select command.
			if (reader.commandId() != null) {
				selectCommand(reader.commandId());
			}

		} catch (Exception exception) {
			Log.error("Failed to restore layout.", exception);
			applyDefaultLayout();
		}

		// Show window.
		mainWindow.setVisible(true);
	}

	public PaneView create(String paneType) {
		Class paneClass = paneTypes.get(paneType);
		if (paneClass == null) {
			throw new RoboticException("The pane type '%s' is not recognized.",
					paneType);
		}
		return (PaneView) container.get(paneClass);
	}

	public void show(String paneType) {
		windowCount++;
		show(paneType, windowCount * 30, windowCount * 30, null);
	}

	public Items<PaneView> panes() {
		return mainWindow.panes();
	}

	public void status(String format, Object... parameters) {
		mainWindow.statusBar().text(String.format(format, parameters));
	}

	public void error(String format, Object... parameters) {
		mainWindow.statusBar().error(String.format(format, parameters));
	}

	private void show(String paneType, int x, int y, Dimension size) {

		// Create pane.
		PaneView pane = create(paneType);

		// Position.
		windowCount++;
		pane.setLocation(x, y);

		// Size.
		if (size != null) {
			pane.setSize(size);
		}

		// Add.
		pane.setVisible(true);
		mainWindow.addToDesktop(pane);
	}

	public void exit() {

		// Save layout.
		try {
			DataService dataService = container.get(DataService.class);
			new SettingsWriter(mainWindow.panes(),
					dataService.selectedCommand()).write(UI_XML_FILE);
		} catch (Exception exception) {
			Log.error("Failed to save layout.", exception);
		}

		// Terminate.
		System.exit(0);
	}

	private void applyDefaultLayout() {

		// Panes.
		show("scene", 7, 13, new Dimension(457, 318));
		show("navigation", 7, 344, new Dimension(307, 302));
		show("command", 477, 13, new Dimension(879, 633));

		// Command.
		DataService dataService = container.get(DataService.class);
		selectCommand(dataService.selectedCommand().id());
	}

	private void selectCommand(int commandId) {
		CommandService commandService = container.get(CommandService.class);
		commandService.select(commandId);
	}

	private void registerPane(String paneType, Class paneClass) {
		paneTypes.put(paneType, paneClass);
	}
}