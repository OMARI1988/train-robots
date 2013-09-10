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

import com.trainrobots.ui.services.CorpusService;

public class CorpusView extends JPanel {

	private final JLabel label = new JLabel();
	private final CorpusService corpusService;

	@Inject
	public CorpusView(CorpusService corpusService) {

		// Services.
		this.corpusService = corpusService;

		// Layout.
		setLayout(null);
		setBackground(Color.BLACK);

		// Label.
		label.setText("No command selected");
		label.setForeground(Color.WHITE);
		Dimension ps = label.getPreferredSize();
		label.setBounds(25, 15, (int) ps.getWidth(), (int) ps.getHeight());
		add(label);
	}

	public void select(int commandId) {
		label.setText("Command " + commandId);
	}
}