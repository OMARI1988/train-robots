/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services;

import java.beans.PropertyVetoException;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.ui.menus.MainMenu;
import com.trainrobots.ui.views.MainWindow;
import com.trainrobots.ui.views.PaneView;
import com.trainrobots.ui.views.RobotView;

public class WindowService {

	private final MainWindow mainWindow;
	private int windowCount;

	public WindowService() {

		// Configure logging.
		Log.configureConsole();

		// Look and feel.
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (Exception exception) {
			throw new RoboticException(exception);
		}

		// Initiate.
		MainMenu menu = new MainMenu(this);
		mainWindow = new MainWindow(menu);
		show(new RobotView());
	}

	public void showMainWindow() {
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