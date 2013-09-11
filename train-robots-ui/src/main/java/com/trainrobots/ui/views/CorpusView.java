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
import java.awt.Font;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.trees.Node;
import com.trainrobots.ui.services.ConfigurationService;

public class CorpusView extends JPanel {

	private final ConfigurationService configurationService;
	private final JLabel headerLabel = new JLabel();
	private final JLabel commandLabel = new JLabel();
	private final JComboBox markList;
	private final EditorView editor = new EditorView();
	private final GraphicsPanel beforePanel;
	private final GraphicsPanel afterPanel;
	private Command command;

	@Inject
	public CorpusView(ConfigurationService configurationService) {

		// Services.
		this.configurationService = configurationService;

		// Layout.
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		setBackground(Color.BLACK);

		// Header.
		headerLabel.setText("No scene selected");
		headerLabel.setForeground(new Color(141, 244, 50));
		headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
		headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		add(headerLabel);

		// Command.
		commandLabel.setText("No command selected");
		commandLabel.setForeground(new Color(200, 200, 200));
		commandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		commandLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		commandLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
		add(commandLabel);

		// List.
		markList = new JComboBox(new String[] { "Unmarked",
				"Inappropriate words or spam", "Invalid spelling or grammar",
				"Before and after images confused",
				"Incorrect or bad directions", "Not specific enough",
				"Accurate" });
		JPanel wrapperPanel = new JPanel();
		wrapperPanel.setMaximumSize(new Dimension(240, 28));
		wrapperPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		wrapperPanel.add(markList);
		wrapperPanel.setBackground(Color.BLACK);
		add(wrapperPanel);

		// Editor.
		JScrollPane scrollPane = new JScrollPane(editor);
		scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		scrollPane.setBackground(Color.BLACK);

		// Scene.
		JPanel scenePanel = new JPanel();
		scenePanel.setLayout(new BoxLayout(scenePanel, BoxLayout.X_AXIS));
		scenePanel.setBackground(Color.BLACK);
		scenePanel.add(scrollPane);
		scenePanel.add(beforePanel = createGraphicsPanel());
		scenePanel.add(afterPanel = createGraphicsPanel());
		afterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		afterPanel.setBackground(Color.BLACK);
		beforePanel.setAlignmentY(Component.TOP_ALIGNMENT);
		afterPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		scenePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(scenePanel);
	}

	public void select(Command command) {

		// Bind.
		this.command = command;

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

	public void parse() {
		if (command == null) {
			return;
		}
		Node node = Parser.parse(command.text);
		editor.setText(node.format());
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
}