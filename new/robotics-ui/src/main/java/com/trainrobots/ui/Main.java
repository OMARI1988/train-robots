/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui;

import static javax.swing.SwingUtilities.invokeLater;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.ui.services.WindowService;

public class Main {

	private Main() {
	}

	public static void main(String[] args) {
		invokeLater(new Runnable() {
			public void run() {
				try {

					// Configure logging.
					Log.configureConsole();

					// Look and feel.
					try {
						UIManager.setLookAndFeel(new NimbusLookAndFeel());
					} catch (Exception exception) {
						throw new RoboticException(exception);
					}

					// Main window.
					Container container = new Container();
					container.get(WindowService.class).showMainWindow();

				} catch (Exception exception) {
					Log.error("Failed start UI.", exception);
				}
			}
		});
	}
}