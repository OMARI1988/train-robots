/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import java.io.InputStream;

import org.xml.sax.Attributes;

import com.trainrobots.XmlReader;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.scenes.Scenes;
import com.trainrobots.tokenizer.Tokenizer;

public class CommandReader {

	private final Scenes scenes;
	private Commands commands;

	public CommandReader(Scenes scenes) {
		this.scenes = scenes;
	}

	public Commands commands() {
		return commands;
	}

	public void readCommands(InputStream stream) {
		ItemsList<Command> commandList = new ItemsList<Command>();
		new XmlReader() {
			protected void handleElementStart(String name, Attributes attributes) {
				if (name.equals("command")) {
					int id = Integer.parseInt(attributes.getValue("id"));
					int sceneId = Integer.parseInt(attributes
							.getValue("sceneId"));
					String text = attributes.getValue("text");
					Items<Terminal> tokens = new Tokenizer(text).tokens();
					commandList.add(new Command(id, scenes.scene(sceneId),
							text, tokens));
				}
			}
		}.read(stream);
		commands = new Commands(commandList);
	}

	public void readLosr(InputStream stream) {
		new XmlReader() {
			protected void handleElementStart(String name, Attributes attributes) {
				if (name.equals("command")) {
					int id = Integer.parseInt(attributes.getValue("id"));
					Losr losr = Losr.parse(attributes.getValue("losr"));
					commands.command(id).losr(losr);
				}
			}
		}.read(stream);
	}
}