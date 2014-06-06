/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import java.nio.file.Paths;

import com.trainrobots.Log;
import com.trainrobots.collections.Items;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.LayoutReader;
import com.trainrobots.scenes.Layouts;
import com.trainrobots.scenes.Scene;
import com.trainrobots.scenes.SceneReader;
import com.trainrobots.scenes.Scenes;

public class Treebank {

	private final String dataPath;
	private final Layouts layouts;
	private final Scenes scenes;
	private final Commands commands;

	public Treebank(String dataPath) {
		this.dataPath = dataPath;
		Log.configureConsole();
		Log.info("Loading treebank...");

		// Layouts.
		LayoutReader layoutReader = new LayoutReader();
		layoutReader.read(file("layouts.xml"));
		layouts = layoutReader.layouts();
		Log.info("Loaded: %s layouts.", layouts.count());

		// Scenes.
		SceneReader sceneReader = new SceneReader(layouts());
		sceneReader.read(file("scenes.xml"));
		scenes = sceneReader.scenes();
		Log.info("Loaded: %s scenes.", scenes.count());

		// Commands.
		CommandReader commandReader = new CommandReader(scenes());
		commandReader.readCommands(file("commands.xml"));
		commandReader.readLosr(file("losr.xml"));
		commands = commandReader.commands();
		Log.info("Loaded: %s commands.", commands.count());
	}

	public Layout layout(int id) {
		return layouts.layout(id);
	}

	public Layouts layouts() {
		return layouts;
	}

	public Scene scene(int id) {
		return scenes.scene(id);
	}

	public Scenes scenes() {
		return scenes;
	}

	public Command command(int id) {
		return commands.command(id);
	}

	public Commands commands() {
		return commands;
	}

	public Items<Command> commands(Scene scene) {
		return commands.forScene(scene);
	}

	private String file(String filename) {
		return Paths.get(dataPath, filename).toString();
	}
}