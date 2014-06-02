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
import com.trainrobots.treebank.CommandReader;
import com.trainrobots.treebank.Commands;
import com.trainrobots.treebank.LosrReader;

public class RoboticSystem {

	private final String dataPath;
	private Layouts layouts;
	private Scenes scenes;
	private Commands commands;

	public RoboticSystem(String dataPath) {
		this.dataPath = dataPath;
		Log.configureConsole();
		Log.info("Starting robotic system...");
	}

	public Layouts layouts() {
		if (layouts == null) {
			LayoutReader reader = new LayoutReader();
			reader.read(file("layouts.xml"));
			layouts = reader.layouts();
			Log.info("Loaded: %s layouts.", layouts.count());
		}
		return layouts;
	}

	public Scenes scenes() {
		if (scenes == null) {
			SceneReader reader = new SceneReader(layouts());
			reader.read(file("scenes.xml"));
			scenes = reader.scenes();
			Log.info("Loaded: %s scenes.", scenes.count());
		}
		return scenes;
	}

	public Commands commands() {
		if (commands == null) {

			// Commands.
			CommandReader commandReader = new CommandReader(scenes());
			commandReader.read(file("commands.xml"));
			commands = commandReader.commands();
			Log.info("Loaded: %s commands.", commands.count());

			// LOSR.
			LosrReader losrReader = new LosrReader(commands);
			losrReader.read(file("losr.xml"));
			Log.info("Loaded: %s statements.", losrReader.count());
		}
		return commands;
	}

	private String file(String filename) {
		return Paths.get(dataPath, filename).toString();
	}
}