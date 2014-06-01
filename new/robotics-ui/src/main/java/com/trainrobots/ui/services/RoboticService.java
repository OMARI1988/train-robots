/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services;

import com.trainrobots.RoboticSystem;
import com.trainrobots.scenes.Scene;

public class RoboticService {

	private final RoboticSystem system = new RoboticSystem("../.data");
	private Scene selectedScene;

	public RoboticService() {
		selectedScene = system.scenes().of(879);
	}

	public Scene selectedScene() {
		return selectedScene;
	}
}