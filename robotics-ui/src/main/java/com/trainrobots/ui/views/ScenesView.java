/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;

import com.trainrobots.RoboticSystem;
import com.trainrobots.scenes.Scene;
import com.trainrobots.scenes.Scenes;
import com.trainrobots.treebank.Commands;
import com.trainrobots.ui.Styles;
import com.trainrobots.ui.services.data.DataService;

public class ScenesView extends PaneView {

	public ScenesView(DataService dataService) {
		super("Scenes");

		// Initiate.
		setSize(200, 500);
		setLayout(new BorderLayout());

		// Scenes.
		RoboticSystem system = dataService.system();
		Scenes scenes = system.scenes();

		// Model.
		ListModel<Scene> model = new AbstractListModel<Scene>() {

			public int getSize() {
				return scenes.count();
			}

			public Scene getElementAt(int index) {
				return scenes.get(index);
			}
		};

		// List.
		JList<Scene> list = new JList<Scene>(model);
		list.setCellRenderer(new SceneRenderer(system.commands()));

		// Scroll pane.
		JScrollPane scrollPane = new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public String paneType() {
		return "scenes";
	}

	private static class SceneRenderer extends JPanel implements
			ListCellRenderer<Scene> {

		private final Commands commands;
		private final JLabel left = new JLabel();
		private final JLabel right = new JLabel();

		public SceneRenderer(Commands commands) {

			// Initiate.
			this.commands = commands;

			// Left.
			setLayout(new BorderLayout());
			add(left, BorderLayout.WEST);
			left.setPreferredSize(new Dimension(40, 0));

			// Right.
			add(right, BorderLayout.CENTER);

			// Component.
			setBorder(new EmptyBorder(1, 5, 1, 5));
		}

		public Component getListCellRendererComponent(JList list, Scene scene,
				int index, boolean isSelected, boolean cellHasFocus) {

			// Scene ID.
			left.setText(Integer.toString(scene.id()));

			// Commands.
			int commands = this.commands.forScene(scene).count();
			right.setText(commands + (commands == 1 ? " command" : " commands"));

			// Selected?
			if (isSelected) {
				left.setForeground(Color.WHITE);
				right.setForeground(Color.WHITE);
				setBackground(Styles.SELECTED_COLOR);
			} else {
				left.setForeground(Color.BLUE);
				right.setForeground(Color.BLACK);
				setBackground(index % 2 == 0 ? Color.WHITE
						: Styles.ALTERNATE_COLOR);
			}
			return this;
		}
	}
}