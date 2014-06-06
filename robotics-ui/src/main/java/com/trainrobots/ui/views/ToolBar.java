/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.VERTICAL_CENTER;
import static javax.swing.SpringLayout.WEST;

import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.window.WindowService;

public class ToolBar extends JPanel {

	private final WindowService windowService;
	private final CommandService commandService;

	public ToolBar(WindowService windowService, CommandService commandService) {

		// Services.
		this.windowService = windowService;
		this.commandService = commandService;

		// Layout.
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		// Combo box.
		JComboBox<String> comboBox = new JComboBox<String>(new String[] {
				"Command", "Scene" });
		add(comboBox);

		// Search box.
		JTextField searchBox = new JTextField(null, 14);
		searchBox.setMargin(new Insets(0, 1, 0, 1));
		add(searchBox);

		// Constraints.
		layout.putConstraint(WEST, comboBox, 6, WEST, this);
		layout.putConstraint(WEST, searchBox, 4, EAST, comboBox);
		layout.putConstraint(NORTH, searchBox, 2, NORTH, this);
		layout.putConstraint(SOUTH, this, 3, SOUTH, searchBox);
		layout.putConstraint(VERTICAL_CENTER, comboBox, 0, VERTICAL_CENTER,
				searchBox);

		// Key listener.
		searchBox.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					search(searchBox.getText(), comboBox.getSelectedItem()
							.equals("Command"));
				}
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}
		});
	}

	private void search(String text, boolean command) {

		// Empty?
		if (text == null) {
			return;
		}
		text = text.trim();
		if (text.length() == 0) {
			return;
		}

		// Parse.
		int id;
		try {
			id = Integer.parseInt(text);
		} catch (NumberFormatException exception) {
			windowService.error("The %s ID '%s' is not recognized.",
					command ? "command" : "scene", text);
			return;
		}

		// Search.
		try {
			if (command) {
				commandService.command(id);
			} else {
				commandService.scene(id);
			}
		} catch (Exception exception) {
			windowService.error(exception.getMessage());
		}
	}
}