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

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.trainrobots.ui.resources.ResourceUtil;

public class StatusBar extends JPanel {

	private final JLabel label;

	private static final ImageIcon TICK_IMAGE = new ImageIcon(
			ResourceUtil.getUrl("/com/trainrobots/ui/tick.gif"));

	private static final ImageIcon ERROR_IMAGE = new ImageIcon(
			ResourceUtil.getUrl("/com/trainrobots/ui/error.gif"));

	@Inject
	public StatusBar() {

		// Layout.
		setLayout(new BorderLayout());

		// Label.
		label = new JLabel();
		label.setBorder(new EmptyBorder(4, 5, 4, 0));
		add(label, BorderLayout.WEST);

		// Ready.
		setText("Ready");
	}

	public void setText(String text) {
		label.setText(text);
		label.setIcon(TICK_IMAGE);
	}

	public void setError(String text) {
		label.setText(text);
		label.setIcon(ERROR_IMAGE);
	}
}