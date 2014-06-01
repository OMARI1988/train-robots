/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services;

import java.beans.PropertyVetoException;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.ui.menus.MainMenu;
import com.trainrobots.ui.views.MainWindow;
import com.trainrobots.ui.views.RobotWindow;

public class WindowService {

	private final MainWindow mainWindow;
	private int windowCount;

	public WindowService() {

		// Configure logging.
		Log.configureConsole();

		// Set look and feel.
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (Exception exception) {
			throw new RoboticException(exception);
		}

		// Initiate.
		MainMenu menu = new MainMenu(this);
		mainWindow = new MainWindow(menu);
		showRobotWindow();
	}

	public void showMainWindow() {
		mainWindow.setVisible(true);
	}

	public void showRobotWindow() {

		// Position window.
		RobotWindow window = new RobotWindow();
		windowCount++;
		window.setLocation(windowCount * 30, windowCount * 30);

		// Add.
		window.setVisible(true);
		mainWindow.addToDesktop(window);

		// Focus.
		try {
			window.setSelected(true);
		} catch (PropertyVetoException exception) {
			throw new RoboticException(exception);
		}
	}
}