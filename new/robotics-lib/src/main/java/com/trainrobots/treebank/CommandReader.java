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

public class CommandReader extends XmlReader {

	private final Scenes scenes;
	private final ItemsList<Command> commands = new ItemsList<Command>();

	public CommandReader(Scenes scenes) {
		this.scenes = scenes;
	}

	public Commands commands() {
		return new Commands(commands);
	}

	@Override
	protected void handleElementStart(String name, Attributes attributes) {

		if (name.equals("command")) {
			int id = Integer.parseInt(attributes.getValue("id"));
			int sceneId = Integer.parseInt(attributes.getValue("sceneId"));
			String text = attributes.getValue("text");
			commands.add(new Command(id, scenes.scene(sceneId), text));
		}
	}
}