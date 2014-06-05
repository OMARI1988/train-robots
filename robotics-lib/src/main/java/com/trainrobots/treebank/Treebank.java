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
	private Layouts layouts;
	private Scenes scenes;
	private Commands commands;

	public Treebank(String dataPath) {
		this.dataPath = dataPath;
		Log.configureConsole();
		Log.info("Loading treebank...");
	}

	public Layout layout(int id) {
		return layouts().layout(id);
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

	public Scene scene(int id) {
		return scenes().scene(id);
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

	public Command command(int id) {
		return commands().command(id);
	}

	public Commands commands() {
		if (commands == null) {
			CommandReader reader = new CommandReader(scenes());
			reader.readCommands(file("commands.xml"));
			reader.readLosr(file("losr.xml"));
			commands = reader.commands();
			Log.info("Loaded: %s commands.", commands.count());
		}
		return commands;
	}

	public Items<Command> commands(Scene scene) {
		return commands().forScene(scene);
	}

	private String file(String filename) {
		return Paths.get(dataPath, filename).toString();
	}
}