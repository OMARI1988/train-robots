/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XmlReader {

	public void read(String filename) {
		read(new InputSource(filename));
	}

	public void read(InputStream stream) {
		read(new InputSource(stream));
	}

	protected abstract void handleElementStart(String name,
			Attributes attributes);

	protected void handleElementEnd(String name) {
	}

	private void read(InputSource source) {
		try {

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(source, new DefaultHandler() {

				public void startElement(String uri, String localName,
						String name, Attributes attributes) {
					handleElementStart(name, attributes);
				}

				public void endElement(String uri, String localName, String name) {
					handleElementEnd(name);
				}
			});

		} catch (Exception exception) {
			throw new RoboticException(exception);
		}
	}
}