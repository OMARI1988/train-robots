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

public class DataService {

	private final RoboticSystem system = new RoboticSystem("../.data");
	private Scene selectedScene;

	public DataService() {
		selectedScene = system.scenes().scene(879);
	}

	public RoboticSystem system() {
		return system;
	}

	public Scene selectedScene() {
		return selectedScene;
	}
}