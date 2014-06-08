/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui;

import static org.picocontainer.Characteristics.CACHE;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import com.trainrobots.RoboticException;
import com.trainrobots.ui.menus.MainMenu;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.views.MainWindow;
import com.trainrobots.ui.views.RobotView;
import com.trainrobots.ui.views.ToolBar;
import com.trainrobots.ui.views.command.CommandView;
import com.trainrobots.ui.views.navigation.NavigationView;
import com.trainrobots.ui.views.scene.SceneView;

public class Container {

	private final MutablePicoContainer pico = new DefaultPicoContainer();

	public Container() {

		// Services.
		registerSingle(this);
		registerSingle(WindowService.class);
		registerSingle(TreebankService.class);
		registerSingle(CommandService.class);

		// UI components.
		registerSingle(MainWindow.class);
		registerSingle(MainMenu.class);
		registerSingle(ToolBar.class);

		// Views.
		register(RobotView.class);
		register(SceneView.class);
		register(NavigationView.class);
		register(CommandView.class);

		// Register main window.
		get(WindowService.class).setMainWindow(get(MainWindow.class));
	}

	public <T> T get(Class<T> type) {
		T instance = pico.getComponent(type);
		if (instance == null) {
			throw new RoboticException("Failed to create instance of %s.", type);
		}
		return instance;
	}

	private void registerSingle(Object object) {
		pico.as(CACHE).addComponent(object);
	}

	private void register(Object object) {
		pico.addComponent(object);
	}
}