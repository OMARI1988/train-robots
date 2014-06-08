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

public abstract class Theme {

	private final Color skip = new Color(120, 120, 120);

	public abstract Font font();

	public abstract Color foreground();

	public abstract Color background();

	public abstract Color entity();

	public abstract Color event();

	public abstract Color spatialRelation();

	public abstract Color boundingBox();

	public Color skip() {
		return skip;
	}

	public boolean detailedTags() {
		return false;
	}
}