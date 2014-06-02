/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.window;

import org.xml.sax.Attributes;

import com.trainrobots.XmlReader;

public class PaneReader extends XmlReader {

	private final PaneBuilder builder;

	public PaneReader(PaneBuilder builder) {
		this.builder = builder;
	}

	@Override
	protected void handleElementStart(String name, Attributes attributes) {

		if (!name.equals("panes")) {
			int x = Integer.parseInt(attributes.getValue("x"));
			int y = Integer.parseInt(attributes.getValue("y"));
			int width = Integer.parseInt(attributes.getValue("width"));
			int height = Integer.parseInt(attributes.getValue("height"));
			builder.build(name, x, y, width, height);
		}
	}
}