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

import com.trainrobots.ui.menus.MainMenu;
import com.trainrobots.ui.services.RoboticService;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.MainWindow;

public class Container {

	private final MutablePicoContainer pico = new DefaultPicoContainer();

	public Container() {

		// Services.
		registerSingleInstance(WindowService.class);
		registerSingleInstance(RoboticService.class);

		// UI components.
		registerSingleInstance(MainWindow.class);
		registerSingleInstance(MainMenu.class);

		// Register main window.
		get(WindowService.class).setMainWindow(get(MainWindow.class));
	}

	public <T> T get(Class<T> type) {
		return pico.getComponent(type);
	}

	private <T> void registerSingleInstance(Class<T> type) {
		pico.as(CACHE).addComponent(type);
	}
}