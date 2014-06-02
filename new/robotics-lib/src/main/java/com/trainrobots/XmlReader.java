/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XmlReader {

	public void read(String filename) {
		try {

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(filename, new DefaultHandler() {

				public void startElement(String uri, String localName,
						String name, Attributes attributes) {
					handleStartElement(name, attributes);
				}

				public void endElement(String uri, String localName, String name) {
					handleEndElement(name);
				}
			});

		} catch (Exception exception) {
			throw new RoboticException(exception);
		}
	}

	protected abstract void handleStartElement(String name,
			Attributes attributes);

	protected void handleEndElement(String name) {
	}
}