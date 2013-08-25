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

import java.awt.Color;
import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SceneView extends JPanel {

	private final JLabel label1 = new JLabel();
	private final JLabel label2 = new JLabel();
	private final GraphicsPanel panel1 = new GraphicsPanel();
	private final GraphicsPanel panel2 = new GraphicsPanel();

	@Inject
	public SceneView() {

		// Layout.
		setLayout(null);
		setBackground(Color.WHITE);

		// Label 1.
		label1.setText("No scene selected");
		Dimension ps1 = label1.getPreferredSize();
		label1.setBounds(24, 15, (int) ps1.getWidth(), (int) ps1.getHeight());
		add(label1);

		// Label 2.
		label2.setText("No scene selected");
		Dimension ps2 = label2.getPreferredSize();
		label2.setBounds(346, 15, (int) ps2.getWidth(), (int) ps2.getHeight());
		add(label2);

		// Panel 1.
		panel1.setBounds(24, 32, 300, 300);
		add(panel1);

		// Panel 2.
		panel2.setBounds(346, 32, 300, 300);
		add(panel2);
	}

	public void select(int groupNumber, int imageNumber) {
		label1.setText(groupNumber + ".1");
		label2.setText(groupNumber + "." + imageNumber);
	}
}