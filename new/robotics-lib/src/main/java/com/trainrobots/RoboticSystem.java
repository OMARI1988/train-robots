/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import java.nio.file.Paths;

import com.trainrobots.scenes.LayoutReader;
import com.trainrobots.scenes.Layouts;
import com.trainrobots.scenes.SceneReader;
import com.trainrobots.scenes.Scenes;

public class RoboticSystem {

	private final String dataPath;
	private Layouts layouts;
	private Scenes scenes;

	public RoboticSystem(String dataPath) {
		this.dataPath = dataPath;
		Log.configureConsole();
		Log.info("Starting robotic system...");
	}

	public Layouts layouts() {
		if (layouts == null) {
			Log.info("Loading layouts...");
			LayoutReader reader = new LayoutReader(file("layouts.xml"));
			layouts = reader.layouts();
			Log.info("Loaded: %s layouts.", layouts.count());
		}
		return layouts;
	}

	public Scenes scenes() {
		if (scenes == null) {
			Log.info("Loading scenes...");
			SceneReader reader = new SceneReader(file("scenes.xml"), layouts());
			scenes = reader.scenes();
			Log.info("Loaded: %s scenes.", scenes.count());
		}
		return scenes;
	}

	private String file(String filename) {
		return Paths.get(dataPath, filename).toString();
	}
}