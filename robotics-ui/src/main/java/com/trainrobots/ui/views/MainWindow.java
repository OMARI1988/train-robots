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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.Painter;
import javax.swing.UIDefaults;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.ui.Resources;
import com.trainrobots.ui.menus.MainMenu;
import com.trainrobots.ui.services.window.WindowService;

public class MainWindow extends JFrame {

	private final JDesktopPane desktop;

	public MainWindow(WindowService windowService, MainMenu menu) {
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
					Resources.url("/com/trainrobots/ui/icons/go" + size
							+ ".png")));
		}
		setIconImages(images);

		// Size and position.
		setSize(650, 500);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);

		// Window listener.
		addWindowListener(new WindowListener() {

			public void windowOpened(WindowEvent event) {
			}

			public void windowClosing(WindowEvent event) {
				windowService.exit();
			}

			public void windowClosed(WindowEvent event) {
			}

			public void windowIconified(WindowEvent event) {
			}

			public void windowDeiconified(WindowEvent event) {
			}

			public void windowActivated(WindowEvent event) {
			}

			public void windowDeactivated(WindowEvent event) {
			}
		});
	}

	public void addToDesktop(PaneView pane) {

		// Always behind?
		if (pane.alwaysBehind()) {
			desktop.setLayer(pane, -1);
		}

		// Add.
		desktop.add(pane);

		// Focus.
		try {
			pane.setSelected(true);
		} catch (PropertyVetoException exception) {
			throw new RoboticException(exception);
		}
	}

	public Items<PaneView> panes() {
		return new ItemsArray(desktop.getAllFrames());
	}
}