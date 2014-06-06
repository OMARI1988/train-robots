/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.window;

import java.awt.Dimension;

public class PaneLayout {

	private final int x;
	private final int y;
	private final Dimension size;

	public PaneLayout(int x, int y, Dimension size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public Dimension size() {
		return size;
	}
}