/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.SOUTH;

import java.awt.Container;

import javax.swing.Spring;
import javax.swing.SpringLayout;

import com.trainrobots.scenes.Scene;
import com.trainrobots.ui.FractionSpring;
import com.trainrobots.ui.services.RoboticService;

public class SceneView extends PaneView {

	public SceneView(RoboticService roboticService) {
		this(roboticService.selectedScene());
	}

	public SceneView(Scene scene) {
		super("Scene " + scene.id());

		// Initiate.
		setSize(600, 330);

		// Layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		Container content = getContentPane();
		Spring half = FractionSpring.half(layout.getConstraint(EAST, content));

		// Before.
		LayoutView before = new LayoutView(scene.before());
		add(before,
				new SpringLayout.Constraints(Spring.constant(0), Spring
						.constant(0), half, layout
						.getConstraint(SOUTH, content)));

		// After.
		LayoutView after = new LayoutView(scene.after());
		add(after, new SpringLayout.Constraints(half, Spring.constant(0), half,
				layout.getConstraint(SOUTH, content)));
	}
}