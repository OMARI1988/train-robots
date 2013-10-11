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
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.trainrobots.core.configuration.Configuration;
import com.trainrobots.nlp.parser.GoldParser;
import com.trainrobots.nlp.parser.ParserResult;
import com.trainrobots.nlp.processor.Planner;
import com.trainrobots.nlp.processor.Processor;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.Move;

public class InteractiveView extends JPanel {

	private final JTextArea textArea = new JTextArea();
	private final JTextField textField = new JTextField();
	private final GraphicsPanel graphicsPanel;
	private WorldModel world;

	@Inject
	public InteractiveView() {

		setLayout(new BorderLayout());
		setBackground(Color.BLACK);

		JPanel left = new JPanel();
		left.setLayout(new BorderLayout());

		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setForeground(new Color(220, 220, 220));
		textArea.setBackground(Color.BLACK);
		textArea.setCaretColor(Color.WHITE);
		textArea.setTabSize(4);
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBackground(Color.BLACK);
		left.add(scrollPane, BorderLayout.CENTER);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		textField.setMargin(new Insets(5, 2, 5, 2));
		textField.setFont(new Font("Consolas", Font.PLAIN, 12));
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				handleCommand();
			}
		});
		left.add(textField, BorderLayout.SOUTH);

		JPanel right = new JPanel();
		graphicsPanel = new GraphicsPanel(325, 350);
		graphicsPanel.setBounds(100, 25, (int) (325 * 1.5), (int) (350 * 1.5));
		right.setLayout(null);
		right.add(graphicsPanel);
		right.setBackground(Color.BLACK);

		JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left,
				right);
		splitter.setDividerLocation(750);
		splitter.setDividerSize(2);
		add(splitter, BorderLayout.CENTER);

		randomScene();
	}

	public void randomScene() {
		setScene(1 + (int) (1000 * Math.random()));
	}

	public void setScene(int sceneNumber) {
		writeLine("Scene " + sceneNumber);
		world = SceneManager.getScene(sceneNumber).before.clone();
		renderWorld();
	}

	private void handleCommand() {

		String text = textField.getText();
		if (text == null) {
			return;
		}
		text = text.trim();
		if (text.length() == 0) {
			return;
		}

		writeLine();
		writeLine("> " + text);
		textField.setText("");

		try {
			processCommand(text);
			renderWorld();
		} catch (Exception exception) {
			exception.printStackTrace();
			writeLine(exception.getMessage());
		}
	}

	private void processCommand(String text) {

		ParserResult result = GoldParser.parse(world, text);
		if (result.rcl() == null) {
			writeLine(result.reason());
			return;
		}
		writeLine(result.rcl().format().replace("\r", ""));

		List<Move> moves = new Planner(world).getMoves(result.rcl());
		if (moves == null || moves.size() == 0) {
			writeLine("Failed to plan move.");
			return;
		}

		new Processor(world).execute(moves);
		writeLine("OK");
	}

	private void writeLine() {
		writeLine("");
	}

	private void writeLine(String text) {
		textArea.append(text + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	private void renderWorld() {
		Configuration configuration = world.toConfiguration();
		graphicsPanel.getRobotControl().loadConfiguration(configuration);
	}
}