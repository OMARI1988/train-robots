/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.trainrobots.scenes.Scene;

public class MainWindow extends JFrame {

	public MainWindow() {

		// Close.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Size.
		setSize(500, 500);

		// Scene.
		Scene scene = new Scene();
		add(new SceneView(scene), BorderLayout.CENTER);

		// Controls.
		add(new ControlView(scene), BorderLayout.SOUTH);
	}
}