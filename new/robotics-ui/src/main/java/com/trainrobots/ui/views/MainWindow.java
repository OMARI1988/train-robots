/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.Painter;
import javax.swing.UIDefaults;

import com.trainrobots.ui.Resources;
import com.trainrobots.ui.menus.MainMenu;

public class MainWindow extends JFrame {

	private final JDesktopPane desktop;

	public MainWindow(MainMenu menu) {
		super("Train Robots");

		// Menu.
		setJMenuBar(menu);

		// Desktop.
		desktop = new JDesktopPane() {

			public void updateUI() {
				UIDefaults map = new UIDefaults();
				Painter<JComponent> painter = new Painter<JComponent>() {
					public void paint(Graphics2D g, JComponent c, int w, int h) {
						g.setColor(new Color(80, 80, 80));
						g.fillRect(0, 0, w, h);
					}
				};
				map.put("DesktopPane[Enabled].backgroundPainter", painter);
				putClientProperty("Nimbus.Overrides", map);
				super.updateUI();
			}
		};
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

	public void addToDesktop(PaneView pane) {
		desktop.add(pane);
	}
}