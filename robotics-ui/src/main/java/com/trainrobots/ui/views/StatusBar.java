/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.trainrobots.ui.Resources;
import com.trainrobots.ui.services.validation.ValidationService;

public class StatusBar extends JPanel {

	private final JLabel label;

	private static final ImageIcon TICK_IMAGE = new ImageIcon(
			Resources.url("/com/trainrobots/ui/icons/tick.gif"));

	private static final ImageIcon ERROR_IMAGE = new ImageIcon(
			Resources.url("/com/trainrobots/ui/icons/error.gif"));

	public StatusBar(ValidationService validationService) {

		// Layout.
		setLayout(new BorderLayout());

		// Label.
		label = new JLabel();
		label.setBorder(new EmptyBorder(4, 5, 4, 0));
		add(label, BorderLayout.WEST);

		// Ready.
		text("Ready");

		// Mouse.
		this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent event) {
				validationService.navigate();
			}

			public void mousePressed(MouseEvent event) {
			}

			public void mouseReleased(MouseEvent event) {
			}

			public void mouseEntered(MouseEvent event) {
			}

			public void mouseExited(MouseEvent event) {
			}
		});
	}

	public void text(String text) {
		label.setText(text);
		label.setIcon(TICK_IMAGE);
	}

	public void error(String text) {
		label.setText(text);
		label.setIcon(ERROR_IMAGE);
	}
}