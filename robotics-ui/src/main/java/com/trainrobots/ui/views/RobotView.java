/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.trainrobots.scenes.Gripper;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;

public class RobotView extends PaneView {

	private final Layout layout = new Layout();

	public RobotView() {
		super("Robot");

		// Initiate.
		setLayout(new BorderLayout());

		// Scene.
		add(new LayoutView(layout), BorderLayout.CENTER);

		// Label.
		JLabel label = new JLabel("Keys: arrow keys, q, a and space.",
				SwingConstants.CENTER);
		label.setBorder(new EmptyBorder(5, 0, 3, 0));
		label.setOpaque(true);
		label.setBackground(Color.BLACK);
		label.setForeground(Color.green.brighter());
		add(label, BorderLayout.SOUTH);

		// Keys.
		bindKey("SPACE", () -> {
			Gripper gripper = layout.gripper();
			gripper.open(!gripper.open());
		});
		bindKey("UP", () -> moveGripper(-1, 0, 0));
		bindKey("DOWN", () -> moveGripper(1, 0, 0));
		bindKey("LEFT", () -> moveGripper(0, -1, 0));
		bindKey("RIGHT", () -> moveGripper(0, 1, 0));
		bindKey("Q", () -> moveGripper(0, 0, 1));
		bindKey("A", () -> moveGripper(0, 0, -1));
	}

	@Override
	public String paneType() {
		return "robot";
	}

	private void moveGripper(int dx, int dy, int dz) {
		Gripper gripper = layout.gripper();
		Position p = gripper.position().add(dx, dy, dz);
		if (p.x() >= 0 && p.x() < 8 && p.y() >= 0 && p.y() < 8 && p.z() >= 0
				&& p.z() < 8) {
			gripper.position(p);
		}
	}
}