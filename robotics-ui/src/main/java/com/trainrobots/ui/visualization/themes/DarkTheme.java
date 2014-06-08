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

public class DarkTheme extends Theme {

	private final Font font = new Font("Arial", Font.PLAIN, 16);
	private final Color foreground = new Color(200, 200, 200);
	private final Color background = new Color(33, 33, 33);
	private final Color entity = new Color(35, 206, 235);
	private final Color event = new Color(255, 165, 0);
	private final Color spatialRelation = new Color(141, 244, 50);
	private final Color boundingBox = new Color(153, 217, 234);

	@Override
	public Font font() {
		return font;
	}

	@Override
	public Color foreground() {
		return foreground;
	}

	@Override
	public Color background() {
		return background;
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
		return boundingBox;
	}
}