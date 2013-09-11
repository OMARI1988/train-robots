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
import java.awt.Component;
import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.ui.services.ConfigurationService;

public class CorpusView extends JPanel {

	private final ConfigurationService configurationService;
	private final JLabel headerLabel = new JLabel();
	private final JLabel commandLabel = new JLabel();
	private final GraphicsPanel beforePanel;
	private final GraphicsPanel afterPanel;

	@Inject
	public CorpusView(ConfigurationService configurationService) {

		this.configurationService = configurationService;
		// Layout.
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		setBackground(Color.BLACK);

		// Header.
		headerLabel.setText("No scene selected");
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(headerLabel);

		// Command.
		commandLabel.setText("No command selected");
		commandLabel.setForeground(Color.WHITE);
		commandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(commandLabel);

		// Scene.
		JPanel scenePanel = new JPanel();
		scenePanel.setLayout(new BoxLayout(scenePanel, BoxLayout.X_AXIS));
		scenePanel.setBackground(Color.YELLOW);
		scenePanel.add(beforePanel = createGraphicsPanel());
		scenePanel.add(afterPanel = createGraphicsPanel());
		scenePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(scenePanel);
	}

	private static GraphicsPanel createGraphicsPanel() {
		final int width = 325;
		final int height = 350;
		GraphicsPanel graphicsPanel = new GraphicsPanel(width, height);
		graphicsPanel.setPreferredSize(new Dimension(width, height));
		graphicsPanel.setMaximumSize(new Dimension(width, height));
		graphicsPanel.setMinimumSize(new Dimension(width, height));
		return graphicsPanel;
	}

	public void select(Command command) {

		// Header.
		int sceneNumber = command.sceneNumber;
		headerLabel.setText("Scene " + sceneNumber + ". Command " + command.id
				+ ".");

		// Command.
		commandLabel.setText(command.text);

		// Before.
		beforePanel.getRobotControl().loadConfiguration(
				configurationService.getBefore(sceneNumber));

		// After.
		afterPanel.getRobotControl().loadConfiguration(
				configurationService.getAfter(sceneNumber));
	}
}