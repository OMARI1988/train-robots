/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services;

import java.beans.PropertyVetoException;

import com.trainrobots.RoboticException;
import com.trainrobots.ui.views.CommandsView;
import com.trainrobots.ui.views.MainWindow;
import com.trainrobots.ui.views.PaneView;
import com.trainrobots.ui.views.SceneView;

public class WindowService {

	private final DataService dataService;
	private MainWindow mainWindow;
	private int windowCount;

	public WindowService(DataService dataService) {
		this.dataService = dataService;
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void showMainWindow() {
		show(new CommandsView(dataService));
		show(new SceneView(dataService));
		mainWindow.setVisible(true);
	}

	public void show(PaneView pane) {

		// Position window.
		windowCount++;
		pane.setLocation(windowCount * 30, windowCount * 30);

		// Add.
		pane.setVisible(true);
		mainWindow.addToDesktop(pane);

		// Focus.
		try {
			pane.setSelected(true);
		} catch (PropertyVetoException exception) {
			throw new RoboticException(exception);
		}
	}
}