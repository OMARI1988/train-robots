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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.MarkType;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.parser.GoldParser;
import com.trainrobots.nlp.parser.ParserResult;
import com.trainrobots.nlp.planning.MoveValidator;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.tokenizer.TokenAligner;
import com.trainrobots.nlp.tokenizer.TokenizedGenerationContext;
import com.trainrobots.ui.services.ConfigurationService;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.tree.corpus.CorpusTreeView;

public class CorpusView extends JPanel {

	private final ConfigurationService configurationService;
	private final WindowService windowService;
	private final JLabel headerLabel = new JLabel();
	private final JLabel commandLabel = new JLabel();
	private final JLabel infoLabel = new JLabel();
	private final JComboBox markList;
	private final JComboBox enhancementList;
	private final EditorView editor = new EditorView();
	private final GraphicsPanel beforePanel;
	private final GraphicsPanel afterPanel;
	private Command command;

	@Inject
	public CorpusView(WindowService windowService,
			ConfigurationService configurationService) {

		// Services.
		this.windowService = windowService;
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

		// Info.
		infoLabel.setText("No information available");
		infoLabel.setForeground(new Color(220, 220, 220));
		infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
		add(infoLabel);

		// Marks.
		markList = new JComboBox(new String[] { "Not annotated",
				"Inappropriate words or spam", "Invalid spelling or grammar",
				"Before and after images confused",
				"Incorrect or bad directions", "Not specific enough",
				"Accurate" });
		markList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (command != null) {
						command.mark = MarkType.getMark(markList
								.getSelectedIndex());
						syncTreeNode();
						setInfoText();
					}
				}
			}
		});
		{
			JPanel wrapperPanel = new JPanel();
			wrapperPanel.setMaximumSize(new Dimension(240, 28));
			wrapperPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			wrapperPanel.add(markList);
			wrapperPanel.setBackground(Color.BLACK);
			add(wrapperPanel);
		}

		// Enhancements.
		enhancementList = new JComboBox(EnhancementMap.getDescriptions());
		enhancementList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (command != null) {
						command.enhancement = EnhancementMap
								.getId(enhancementList.getSelectedIndex());
						syncTreeNode();
						setInfoText();
					}
				}
			}
		});
		{
			JPanel wrapperPanel = new JPanel();
			wrapperPanel.setMaximumSize(new Dimension(240, 28));
			wrapperPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			wrapperPanel.add(enhancementList);
			wrapperPanel.setBackground(Color.BLACK);
			add(wrapperPanel);
		}

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

	public void update() {
		if (command == null) {
			return;
		}
		validateCommand();
		command.mark = MarkType.getMark(markList.getSelectedIndex());
		command.enhancement = EnhancementMap.getId(enhancementList
				.getSelectedIndex());
	}

	public void select(Command command) {

		// Update.
		update();

		// Bind.
		this.command = command;

		// Header.
		int sceneNumber = command.sceneNumber;
		headerLabel.setText("Command " + command.id + ". Scene " + sceneNumber
				+ ".");
		StatusBar statusBar = windowService.get(AnnotationWindow.class)
				.getStatusBar();
		statusBar.setText(headerLabel.getText());

		// Command.
		commandLabel.setText(command.text);

		// Info.
		setInfoText();

		// Mark.
		markList.setSelectedIndex(command.mark.getValue());

		// Enhancement.
		enhancementList.setSelectedIndex(EnhancementMap
				.getDescriptionIndex(command.enhancement));

		// Before.
		beforePanel.getRobotControl().loadConfiguration(
				configurationService.getBefore(sceneNumber));

		// After.
		afterPanel.getRobotControl().loadConfiguration(
				configurationService.getAfter(sceneNumber));

		// Editor.
		editor.setText(command.rcl != null ? command.rcl.format().replace("\r",
				"") : null);
	}

	public void parse() {
		if (command == null) {
			return;
		}
		StatusBar statusBar = windowService.get(AnnotationWindow.class)
				.getStatusBar();
		try {
			WorldModel world = SceneManager.getScene(command.sceneNumber).before;
			ParserResult result = GoldParser.parse(world, command.text);
			if (result.rcl() == null) {
				throw new CoreException(result.reason());
			}
			editor.setText(result.rcl().format().replace("\r", ""));
			statusBar.setText("Parsed command.");
		} catch (Exception exception) {
			statusBar.setError(exception.getMessage());
		}
	}

	public void align() {
		if (!validateCommand()) {
			return;
		}
		StatusBar statusBar = windowService.get(AnnotationWindow.class)
				.getStatusBar();
		try {
			TokenAligner.align(command.rcl, command.text);
			editor.setText(command.rcl != null ? command.rcl.format().replace(
					"\r", "") : null);
			setInfoText();
			statusBar.setText("Alignment completed.");
		} catch (Exception exception) {
			statusBar.setError(exception.getMessage());
		}
	}

	public boolean validateCommand() {
		if (command == null) {
			return false;
		}
		StatusBar statusBar = windowService.get(AnnotationWindow.class)
				.getStatusBar();
		String text = editor.getText();
		if (text == null || text.length() == 0) {
			statusBar.setText("RCL not specified.");
			return false;
		}
		try {
			command.rcl = Rcl.fromString(text);
			editor.setText(command.rcl.format().replace("\r", ""));
			statusBar.setText("RCL parsed successfully.");
			setInfoText();
			MoveValidator.validate(command.sceneNumber, command.rcl);
			statusBar.setText("RCL executed successfully.");
			return true;
		} catch (Exception exception) {
			statusBar.setError(exception.getMessage());
			return false;
		}
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

	private void syncTreeNode() {

		if (command == null) {
			return;
		}

		CorpusTreeView tree = windowService.get(AnnotationWindow.class)
				.getCorpusTreeView();
		tree.getCommandNode(command).decorate();
		tree.getSceneNode(command.sceneNumber).decorate();

		tree.repaint();
	}

	private void setInfoText() {
		if (command.rcl == null) {
			infoLabel.setText(" ");
			return;
		}
		try {
			infoLabel.setText(generateHtml());
		} catch (Exception exception) {
			infoLabel.setText(exception.getMessage());
		}
	}

	private String generateHtml() {
		StringBuilder text = new StringBuilder();
		text.append("<html>");
		text.append(command.rcl.generate(new TokenizedGenerationContext(
				command.text)));
		replace(text, "[", " <span color='gray'>(");
		replace(text, "]", ")</span>");
		text.append("</html>");
		return text.toString();
	}

	private static void replace(StringBuilder sb, String oldStr, String newStr) {
		int start = -1;
		while ((start = sb.indexOf(oldStr)) > -1) {
			int end = start + oldStr.length();
			sb.replace(start, end, newStr);
		}
	}
}