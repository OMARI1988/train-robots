/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import org.xml.sax.Attributes;

import com.trainrobots.XmlReader;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.scenes.Scenes;

public class CommandReader {

	private final Scenes scenes;
	private final ItemsList<Command> commandList = new ItemsList<Command>();
	private Commands commands;

	public CommandReader(Scenes scenes) {
		this.scenes = scenes;
	}

	public Commands commands() {
		return commands;
	}

	public void readCommands(String filename) {
		new XmlReader() {
			protected void handleElementStart(String name, Attributes attributes) {
				if (name.equals("command")) {
					int id = Integer.parseInt(attributes.getValue("id"));
					int sceneId = Integer.parseInt(attributes
							.getValue("sceneId"));
					String text = attributes.getValue("text");
					commandList
							.add(new Command(id, scenes.scene(sceneId), text));
				}
			}
		}.read(filename);
		commands = new Commands(commandList);
	}

	public void readLosr(String filename) {
		new XmlReader() {
			protected void handleElementStart(String name, Attributes attributes) {
				if (name.equals("command")) {
					int id = Integer.parseInt(attributes.getValue("id"));
					commands.command(id).setLosr(attributes.getValue("losr"));
				}
			}
		}.read(filename);
	}
}