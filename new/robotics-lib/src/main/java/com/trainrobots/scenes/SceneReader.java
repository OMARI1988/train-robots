/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.ItemsList;

public class SceneReader {

	private final Layouts layouts;
	private final ItemsList<Scene> scenes = new ItemsList<Scene>();
	private int sceneId;
	private int beforeId;
	private int afterId;

	public SceneReader(String filename, Layouts layouts) {
		this.layouts = layouts;
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
				beforeId = 0;
				afterId = 0;
				break;
			case "before":
				beforeId = Integer.parseInt(attributes.getValue("layoutId"));
				break;
			case "after":
				afterId = Integer.parseInt(attributes.getValue("layoutId"));
				break;
			}
		}

		@Override
		public void endElement(String uri, String localName, String name) {
			switch (name) {
			case "scene":
				scenes.add(new Scene(sceneId, layouts.layout(beforeId), layouts
						.layout(afterId)));
				break;
			}
		}
	}
}