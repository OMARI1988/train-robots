/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.themes;

import java.awt.Font;

public class DetailTheme extends SimpleTheme {

	private final Font font = new Font("Segoe UI", Font.PLAIN, 12);

	@Override
	public Font font() {
		return font;
	}

	@Override
	public boolean showDetail() {
		return true;
	}
}