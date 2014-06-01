/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.window;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.trainrobots.RoboticException;

public class PaneReader {

	private final PaneBuilder builder;

	public PaneReader(PaneBuilder builder) {
		this.builder = builder;
	}

	public void read(String filename) {
		try {
			XmlHandler handler = new XmlHandler();
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(filename, handler);
		} catch (Exception exception) {
			throw new RoboticException(exception);
		}
	}

	private class XmlHandler extends DefaultHandler {

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) {

			if (!name.equals("panes")) {
				int x = Integer.parseInt(attributes.getValue("x"));
				int y = Integer.parseInt(attributes.getValue("y"));
				int width = Integer.parseInt(attributes.getValue("width"));
				int height = Integer.parseInt(attributes.getValue("height"));
				builder.build(name, x, y, width, height);
			}
		}
	}
}