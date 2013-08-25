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

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.trainrobots.ui.commands.SearchCommand;
import com.trainrobots.ui.resources.ResourceUtil;

public class ToolBar extends LayoutPanel {

	private final SearchCommand searchCommand;
	private JTextField textField;
	private JButton searchButton;

	@Inject
	public ToolBar(SearchCommand searchCommand) {

		// Command.
		this.searchCommand = searchCommand;

		// Text field.
		textField = new JTextField(null, 14);
		add(textField);
		textField.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					ToolBar.this.searchCommand.actionPerformed(null);
				}
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

		});

		textField.setMargin(new Insets(1, 1, 2, 1));
		setEdge(textField, TOP, 4, this, TOP);
		setEdge(textField, LEFT, 4, this, LEFT);

		// Search button.
		searchButton = new JButton("Search", new ImageIcon(
				ResourceUtil.getUrl("/com/trainrobots/ui/execute.gif")));
		searchButton.addActionListener(searchCommand);
		add(searchButton);
		setEdge(searchButton, LEFT, 3, textField, RIGHT);
		setEdge(searchButton, VERTICAL_CENTER, 0, this, VERTICAL_CENTER);
		searchButton.setMargin(new Insets(1, 5, 1, 5));
		searchButton.setPreferredSize(new Dimension(searchButton
				.getPreferredSize().width,
				textField.getPreferredSize().height - 1));

		// Panel height.
		setEdge(this, BOTTOM, 3, textField, BOTTOM);
	}

	public String getText() {
		return textField.getText();
	}
}