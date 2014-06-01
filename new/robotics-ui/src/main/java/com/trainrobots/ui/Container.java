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
import com.trainrobots.ui.services.data.DataService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.views.CommandsView;
import com.trainrobots.ui.views.MainWindow;
import com.trainrobots.ui.views.RobotView;
import com.trainrobots.ui.views.SceneView;
import com.trainrobots.ui.views.ScenesView;

public class Container {

	private final MutablePicoContainer pico = new DefaultPicoContainer();

	public Container() {

		// Services.
		registerSingle(this);
		registerSingle(WindowService.class);
		registerSingle(DataService.class);

		// UI components.
		registerSingle(MainWindow.class);
		registerSingle(MainMenu.class);
		register(RobotView.class);
		register(SceneView.class);
		register(ScenesView.class);
		register(CommandsView.class);

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