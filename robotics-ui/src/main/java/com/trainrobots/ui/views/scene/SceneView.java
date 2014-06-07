/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.scene;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.SOUTH;

import java.awt.Container;

import javax.swing.Spring;
import javax.swing.SpringLayout;

import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandAware;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.views.LayoutView;
import com.trainrobots.ui.views.PaneView;

public class SceneView extends PaneView implements CommandAware {

	private final LayoutView before;
	private final LayoutView after;

	public SceneView(CommandService commandService) {
		this(commandService.command().scene());
	}

	public SceneView(Scene scene) {
		super(title(scene));

		// Layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		Container content = getContentPane();
		Spring half = FractionSpring.half(layout.getConstraint(EAST, content));

		// Before.
		before = new LayoutView(scene.before());
		add(before,
				new SpringLayout.Constraints(Spring.constant(0), Spring
						.constant(0), half, layout
						.getConstraint(SOUTH, content)));

		// After.
		after = new LayoutView(scene.after());
		add(after, new SpringLayout.Constraints(half, Spring.constant(0), half,
				layout.getConstraint(SOUTH, content)));
	}

	@Override
	public String paneType() {
		return "scene";
	}

	@Override
	public void bindTo(Command command) {
		Scene scene = command.scene();
		setTitle(title(scene));
		before.layout(scene.before());
		after.layout(scene.after());
	}

	private static String title(Scene scene) {
		return "Scene " + scene.id();
	}
}