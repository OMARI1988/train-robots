/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import java.nio.file.Paths;

import com.trainrobots.scenes.SceneReader;
import com.trainrobots.scenes.Scenes;

public class RoboticSystem {

	private final String dataPath;
	private Scenes scenes;

	public RoboticSystem(String dataPath) {
		this.dataPath = dataPath;
		Log.configureConsole();
		Log.info("Starting robotic system...");
	}

	public Scenes scenes() {
		if (scenes == null) {
			Log.info("Loading scenes...");
			SceneReader reader = new SceneReader(file("scenes.xml"));
			scenes = reader.scenes();
			Log.info("Loaded %s scenes.", scenes.count());
		}
		return scenes;
	}

	private String file(String filename) {
		return Paths.get(dataPath, filename).toString();
	}
}