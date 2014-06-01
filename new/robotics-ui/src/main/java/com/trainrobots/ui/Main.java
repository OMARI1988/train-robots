/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui;

import static javax.swing.SwingUtilities.invokeLater;

import com.trainrobots.Log;
import com.trainrobots.ui.services.WindowService;

public class Main {

	private Main() {
	}

	public static void main(String[] args) {
		invokeLater(new Runnable() {
			public void run() {
				try {
					WindowService windowService = new WindowService();
					windowService.showMainWindow();
				} catch (Exception exception) {
					Log.error("Failed start UI.", exception);
				}
			}
		});
	}
}