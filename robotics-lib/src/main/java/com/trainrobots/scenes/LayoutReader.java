/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import org.xml.sax.Attributes;

import com.trainrobots.collections.ItemsArray;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.io.XmlReader;
import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Types;

public class LayoutReader extends XmlReader {

	private final ItemsList<Layout> layouts = new ItemsList<Layout>();
	private final ItemsList<Shape> shapes = new ItemsList<Shape>();
	private int layoutId;
	private Position gripperPosition;
	private boolean gripperOpen;

	public Layouts layouts() {
		return new Layouts(layouts);
	}

	@Override
	protected void handleElementStart(String name, Attributes attributes) {

		switch (name) {
		case "layout":
			layoutId = Integer.parseInt(attributes.getValue("id"));
			gripperPosition = null;
			gripperOpen = false;
			shapes.clear();
			break;
		case "gripper":
			readGripper(attributes);
			break;
		case "cube":
			readShape(Types.Cube, attributes);
			break;
		case "prism":
			readShape(Types.Prism, attributes);
			break;
		}
	}

	@Override
	protected void handleElementEnd(String name) {
		switch (name) {
		case "layout":
			layouts.add(new Layout(layoutId, gripperPosition, gripperOpen,
					new ItemsArray(shapes.toArray())));
			break;
		}
	}

	private void readGripper(Attributes attributes) {
		gripperPosition = Position.parse(attributes.getValue("position"));
		gripperOpen = Boolean.parseBoolean(attributes.getValue("open"));
	}

	private void readShape(Types type, Attributes attributes) {
		Colors color = Colors.parse(attributes.getValue("color"));
		Position position = Position.parse(attributes.getValue("position"));
		shapes.add(new Shape(type, color, position));
	}
}