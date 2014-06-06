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

public class SettingsReader extends XmlReader {

	private final PaneBuilder builder;
	private Integer commandId;
	private boolean panes;

	public SettingsReader(PaneBuilder builder) {
		this.builder = builder;
	}

	public Integer commandId() {
		return commandId;
	}

	@Override
	protected void handleElementStart(String name, Attributes attributes) {

		// Panes.
		if (name.equals("panes")) {
			panes = true;
			return;
		}

		// Pane?
		if (panes) {
			int x = Integer.parseInt(attributes.getValue("x"));
			int y = Integer.parseInt(attributes.getValue("y"));
			int width = Integer.parseInt(attributes.getValue("width"));
			int height = Integer.parseInt(attributes.getValue("height"));
			builder.build(name, x, y, width, height);
			return;
		}

		// Handle this here beacuse we have a pane called 'command'.
		if (name.equals("command")) {
			commandId = Integer.parseInt(attributes.getValue("id"));
			return;
		}
	}

	@Override
	protected void handleElementEnd(String name) {
		if (name.equals("panes")) {
			panes = false;
		}
	}
}