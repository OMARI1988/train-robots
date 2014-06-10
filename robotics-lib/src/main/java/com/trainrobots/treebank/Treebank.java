/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.io.ZipEntryWriter;
import com.trainrobots.io.ZipReader;
import com.trainrobots.io.ZipWriter;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.LayoutReader;
import com.trainrobots.scenes.Layouts;
import com.trainrobots.scenes.Scene;
import com.trainrobots.scenes.SceneReader;
import com.trainrobots.scenes.Scenes;

public class Treebank {

	private final String filename;
	private final Layouts layouts;
	private final Scenes scenes;
	private final Commands commands;

	public Treebank(String path) {

		// Load treebank.
		this.filename = Paths.get(path).toAbsolutePath().normalize().toString();
		Log.info("Loading: %s", filename);
		try (ZipReader zip = new ZipReader(filename)) {

			// Layout.
			try (InputStream stream = zip.open("layouts.xml")) {
				LayoutReader reader = new LayoutReader();
				reader.read(stream);
				layouts = reader.layouts();
				Log.info("Loaded: %s layouts.", layouts.count());
			}

			// Scenes.
			try (InputStream stream = zip.open("scenes.xml")) {
				SceneReader reader = new SceneReader(layouts);
				reader.read(stream);
				scenes = reader.scenes();
				Log.info("Loaded: %s scenes.", scenes.count());
			}

			// Commands.
			CommandReader reader = new CommandReader(scenes);
			try (InputStream stream = zip.open("commands.xml")) {
				reader.readCommands(stream);
			}
			try (InputStream stream = zip.open("losr.xml")) {
				reader.readLosr(stream);
			}
			try (InputStream stream = zip.open("comments.xml")) {
				reader.readComments(stream);
			}
			commands = reader.commands();
			Log.info("Loaded: %s commands.", commands.count());
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	public void write() {

		// Write treebank.
		Log.info("Writing: %s", filename);
		CommandWriter writer = new CommandWriter(commands);
		try (ZipWriter zip = new ZipWriter(filename)) {
			try (ZipEntryWriter entry = zip.open("losr.xml")) {
				writer.writeLosr(entry.output());
			}
			try (ZipEntryWriter entry = zip.open("comments.xml")) {
				writer.writeComments(entry.output());
			}
		}
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
}