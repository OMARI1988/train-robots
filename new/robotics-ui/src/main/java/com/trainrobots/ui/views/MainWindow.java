/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import com.trainrobots.ui.Resources;
import com.trainrobots.ui.menus.MainMenu;

public class MainWindow extends JFrame {

	private final JDesktopPane desktop;

	public MainWindow(MainMenu menu) {
		super("Train Robots");

		// Menu.
		setJMenuBar(menu);

		// Desktop.
		desktop = new JDesktopPane();
		setContentPane(desktop);

		// Icons.
		List<Image> images = new ArrayList<Image>();
		for (int size : new int[] { 16, 24, 32, 48, 64 }) {
			images.add(getToolkit().getImage(
					Resources.getUrl("/com/trainrobots/ui/icons/go" + size
							+ ".png")));
		}
		setIconImages(images);

		// Size and position.
		setSize(650, 500);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);

		// Handle close.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void addToDesktop(JInternalFrame window) {
		desktop.add(window);
	}
}