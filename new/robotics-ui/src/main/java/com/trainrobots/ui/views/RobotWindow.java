/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;

import com.trainrobots.scenes.Layout;

public class RobotWindow extends JInternalFrame {

	public RobotWindow() {
		super("Robot", true, true, true, true);

		// Size.
		setSize(300, 300);

		// Layout.
		Layout layout = new Layout();
		add(new LayoutView(layout), BorderLayout.CENTER);

		// Controls.
		add(new ControlView(layout), BorderLayout.SOUTH);
	}
}