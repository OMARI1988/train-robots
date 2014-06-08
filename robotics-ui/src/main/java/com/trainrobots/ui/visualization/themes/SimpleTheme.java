/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.themes;

import java.awt.Color;
import java.awt.Font;

public class SimpleTheme extends Theme {

	private final Font font = new Font("Times New Roman", Font.PLAIN, 16);
	private final Color entity = new Color(0, 112, 192);
	private final Color event = new Color(255, 0, 0);
	private final Color spatialRelation = new Color(0, 176, 80);

	@Override
	public Font font() {
		return font;
	}

	@Override
	public Color foreground() {
		return Color.BLACK;
	}

	@Override
	public Color background() {
		return Color.WHITE;
	}

	@Override
	public Color entity() {
		return entity;
	}

	@Override
	public Color event() {
		return event;
	}

	@Override
	public Color spatialRelation() {
		return spatialRelation;
	}

	@Override
	public Color boundingBox() {
		return Color.BLUE;
	}
}