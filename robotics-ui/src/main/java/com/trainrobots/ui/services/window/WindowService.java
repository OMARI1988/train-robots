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

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.ui.Container;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.views.CommandView;
import com.trainrobots.ui.views.MainWindow;
import com.trainrobots.ui.views.PaneView;
import com.trainrobots.ui.views.RobotView;
import com.trainrobots.ui.views.navigation.NavigationView;
import com.trainrobots.ui.views.scene.SceneView;

public class WindowService {

	private static final String UI_XML_FILE = "../.data/ui.xml";
	private final Map<String, Class<? extends PaneView>> paneTypes = new HashMap<>();
	private final Map<String, PaneLayout> paneLayouts = new HashMap<>();
	private final Container container;
	private MainWindow mainWindow;

	public WindowService(Container container) {

		// Initiate.
		this.container = container;

		// Panes.
		registerPane("robot", RobotView.class, 540, 60, 305, 325);
		registerPane("scene", SceneView.class, 7, 13, 488, 286);
		registerPane("navigation", NavigationView.class, 7, 310, 307, 303);
		registerPane("command", CommandView.class, 505, 13, 850, 602);
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void showMainWindow() {

		// Restore layout.
		try {

			// Load panes.
			SettingsReader reader = new SettingsReader(this::show);
			reader.read(UI_XML_FILE);

			// Select command.
			if (reader.commandId() != null) {
				commandService().command(reader.commandId());
			}

		} catch (Exception exception) {
			Log.error("Failed to restore layout.", exception);
			applyDefaultLayout();
		}

		// Show window.
		mainWindow.setVisible(true);
	}

	public void show(String paneType) {
		PaneLayout layout = paneLayouts.get(paneType);
		if (layout == null) {
			throw new RoboticException("The pane type '%s' is not recognized.",
					paneType);
		}
		show(paneType, layout);
	}

	public Items<PaneView> panes() {
		return mainWindow.panes();
	}

	public <T extends PaneView> T pane(Class<T> paneClass) {
		for (PaneView pane : mainWindow.panes()) {
			if (pane.getClass() == paneClass) {
				return (T) pane;
			}
		}
		return null;
	}

	public void status(String format, Object... parameters) {
		mainWindow.statusBar().text(String.format(format, parameters));
	}

	public void error(String format, Object... parameters) {
		mainWindow.statusBar().error(String.format(format, parameters));
	}

	private void show(String paneType, PaneLayout layout) {

		// Pane class.
		Class<? extends PaneView> paneClass = paneTypes.get(paneType);
		if (paneClass == null) {
			throw new RoboticException("The pane type '%s' is not recognized.",
					paneType);
		}

		// Already exists?
		PaneView pane = pane(paneClass);
		if (pane != null) {
			pane.focus();
			return;
		}

		// Create pane.
		pane = container.get(paneClass);

		// Position.
		pane.setLocation(layout.x(), layout.y());

		// Size.
		if (layout.size() != null) {
			pane.setSize(layout.size());
		}

		// Add.
		pane.setVisible(true);
		mainWindow.addToDesktop(pane);
		pane.focus();

		// Listener.
		pane.addInternalFrameListener(new InternalFrameListener() {
			public void internalFrameOpened(InternalFrameEvent event) {
			}

			public void internalFrameClosing(InternalFrameEvent event) {
			}

			public void internalFrameClosed(InternalFrameEvent event) {
				PaneView pane = (PaneView) event.getInternalFrame();
				paneLayouts.put(pane.paneType(), new PaneLayout(pane.getX(),
						pane.getY(), pane.getSize()));
			}

			public void internalFrameIconified(InternalFrameEvent event) {
			}

			public void internalFrameDeiconified(InternalFrameEvent event) {
			}

			public void internalFrameActivated(InternalFrameEvent event) {
			}

			public void internalFrameDeactivated(InternalFrameEvent event) {
			}
		});
	}

	public void exit() {

		// Save layout.
		try {
			new SettingsWriter(mainWindow.panes(), commandService().command())
					.write(UI_XML_FILE);
		} catch (Exception exception) {
			Log.error("Failed to save layout.", exception);
		}

		// Terminate.
		System.exit(0);
	}

	private void applyDefaultLayout() {

		// Panes.
		show("scene");
		show("navigation");
		show("command");

		// Command.
		commandService().randomCommand();
	}

	private void registerPane(String paneType, Class paneClass, int x, int y,
			int width, int height) {
		paneTypes.put(paneType, paneClass);
		paneLayouts.put(paneType, new PaneLayout(x, y, new Dimension(width,
				height)));
	}

	private CommandService commandService() {
		return container.get(CommandService.class);
	}
}