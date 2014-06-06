/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import javax.swing.JInternalFrame;

public abstract class PaneView extends JInternalFrame {

	protected PaneView(String title) {
		super(title, true, true, true, false);
	}

	public abstract String paneType();

	public boolean alwaysBehind() {
		return false;
	}
}