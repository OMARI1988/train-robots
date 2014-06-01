/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.scenes.Scenes;

public class CommandReader {

	private final Scenes scenes;
	private final ItemsList<Command> commands = new ItemsList<Command>();

	public CommandReader(String filename, Scenes scenes) {
		this.scenes = scenes;
		try {
			XmlHandler handler = new XmlHandler();
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(filename, handler);
		} catch (Exception exception) {
			throw new RoboticException(exception);
		}
	}

	public Commands commands() {
		return new Commands(commands);
	}

	private class XmlHandler extends DefaultHandler {

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) {

			if (name.equals("command")) {
				int id = Integer.parseInt(attributes.getValue("id"));
				int sceneId = Integer.parseInt(attributes.getValue("sceneId"));
				String text = attributes.getValue("text");
				commands.add(new Command(id, scenes.scene(sceneId), text));
			}
		}
	}
}