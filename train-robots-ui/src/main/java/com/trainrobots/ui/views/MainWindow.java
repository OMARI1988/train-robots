/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.swing.JFrame;

import com.trainrobots.ui.menus.MainMenu;
import com.trainrobots.ui.resources.ResourceUtil;
import com.trainrobots.ui.services.WindowService;

public class MainWindow extends JFrame {

	private final WindowService windowService;
	private final MainMenu mainMenu;
	// private final ToolBar toolBar;
	private final StatusBar statusBar;

	// private final GraphicsPanel graphicsPanel;

	@Inject
	public MainWindow(WindowService windowService, MainMenu mainMenu,
	// ToolBar toolBar,
			StatusBar statusBar) {

		// Dependencies.
		this.windowService = windowService;
		this.mainMenu = mainMenu;
		// this.toolBar = toolBar;
		this.statusBar = statusBar;
		// this.graphicsPanel = new GraphicsPanel();

		// Initiate.
		initiateWindow();
	}

	// public ToolBar getToolBar() {
	// return toolBar;
	// }
	//
	// public GraphicsPanel getGraphicsPanel() {
	// return graphicsPanel;
	// }

	public StatusBar getStatusBar() {
		return statusBar;
	}

	private void initiateWindow() {

		// Register.
		windowService.setMainWindow(this);

		// Close.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Icons.
		List<Image> images = new ArrayList<Image>();
		for (int size : new int[] { 16, 24, 32, 48, 6 }) {
			images.add(getToolkit().getImage(
					ResourceUtil.getUrl("/com/trainrobots/ui/go" + size
							+ ".png")));
		}
		setIconImages(images);

		// Title.
		setTitle("Train Robots");

		// Menu.
		setJMenuBar(mainMenu);

		// Tool bar.
		// add(toolBar, BorderLayout.NORTH);

		// Content.
		// add(new JScrollPane(graphicsPanel), BorderLayout.CENTER);

		// Status bar.
		add(statusBar, BorderLayout.SOUTH);

		// Size and position.
		setSize(650, 500);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
	}
}