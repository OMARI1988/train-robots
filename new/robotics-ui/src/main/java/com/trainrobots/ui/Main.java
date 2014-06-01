/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui;

import static javax.swing.SwingUtilities.invokeLater;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.trainrobots.Log;
import com.trainrobots.ui.services.WindowService;

public class Main {

	private Main() {
	}

	public static void main(String[] args) {
		invokeLater(new Runnable() {
			public void run() {
				start();
			}
		});
	}

	private static void start() {
		try {

			// Configure logging.
			Log.configureConsole();

			// Set look and feel.
			UIManager.setLookAndFeel(new NimbusLookAndFeel());

			// Display the main window.
			WindowService windowService = new WindowService();
			windowService.mainWindow().setVisible(true);

		} catch (Exception exception) {
			Log.error("Failed start UI.", exception);
		}
	}
}