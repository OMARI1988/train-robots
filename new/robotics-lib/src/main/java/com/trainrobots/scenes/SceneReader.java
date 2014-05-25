/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.ItemsList;

public class SceneReader {

	private final ItemsList<Scene> scenes = new ItemsList<Scene>();
	private final ItemsList<Shape> shapes = new ItemsList<Shape>();
	private int sceneId;
	private Gripper gripper;

	public SceneReader(String filename) {
		try {
			XmlHandler handler = new XmlHandler();
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(filename, handler);
		} catch (Exception exception) {
			throw new RoboticException(exception);
		}
	}

	public Scenes scenes() {
		return new Scenes(scenes);
	}

	private class XmlHandler extends DefaultHandler {

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) {

			switch (name) {
			case "scene":
				sceneId = Integer.parseInt(attributes.getValue("id"));
				gripper = null;
				shapes.clear();
				break;
			case "gripper":
				readGripper(attributes);
				break;
			case "cube":
				readShape(Type.Cube, attributes);
				break;
			case "prism":
				readShape(Type.Prism, attributes);
				break;
			}
		}

		@Override
		public void endElement(String uri, String localName, String name) {
			switch (name) {
			case "scene":
				scenes.add(new Scene(sceneId, gripper, shapes));
				break;
			}
		}

		private void readGripper(Attributes attributes) {
			Position position = Position.parse(attributes.getValue("position"));
			boolean open = Boolean.parseBoolean(attributes.getValue("open"));
			gripper = new Gripper(position, open);
		}

		private void readShape(Type type, Attributes attributes) {
			Color color = Color.parse(attributes.getValue("color"));
			Position position = Position.parse(attributes.getValue("position"));
			shapes.add(new Shape(type, color, position));
		}
	};
}