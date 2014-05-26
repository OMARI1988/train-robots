/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.trainrobots.scenes.Gripper;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Scene;

public class ControlView extends JPanel {

	private final Scene scene;

	public ControlView(Scene scene) {

		// Scene.
		this.scene = scene;

		// Label.
		add(new JLabel(
				"Use the arrow keys, q and a to move, and space to toggle the gripper."));

		// Keys.
		bindKey("SPACE", () -> {
			Gripper gripper = scene.gripper();
			gripper.open(!gripper.open());
		});
		bindKey("UP", () -> moveGripper(-1, 0, 0));
		bindKey("DOWN", () -> moveGripper(1, 0, 0));
		bindKey("LEFT", () -> moveGripper(0, -1, 0));
		bindKey("RIGHT", () -> moveGripper(0, 1, 0));
		bindKey("Q", () -> moveGripper(0, 0, 1));
		bindKey("A", () -> moveGripper(0, 0, -1));
	}

	private void bindKey(String key, KeyAction keyAction) {
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key),
				key);
		getActionMap().put(key, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				keyAction.execute();
			}
		});
	}

	private void moveGripper(int dx, int dy, int dz) {
		Gripper gripper = scene.gripper();
		Position p = gripper.position().add(dx, dy, dz);
		if (p.x() >= 0 && p.x() < 8 && p.y() >= 0 && p.y() < 8 && p.z() >= 0
				&& p.z() < 8) {
			gripper.position(p);
		}
	}

	@FunctionalInterface
	private interface KeyAction {
		void execute();
	}
}