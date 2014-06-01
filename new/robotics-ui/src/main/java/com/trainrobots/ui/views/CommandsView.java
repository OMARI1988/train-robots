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

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.trainrobots.RoboticSystem;
import com.trainrobots.collections.Items;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.DataService;

public class CommandsView extends PaneView {

	public CommandsView(DataService dataService) {
		super("Commands");

		// Scene.
		Scene scene = dataService.selectedScene();
		RoboticSystem roboticSystem = dataService.system();
		Items<Command> commands = roboticSystem.commands().forScene(scene);

		// Initiate.
		setSize(300, 300);
		setLayout(new BorderLayout());

		// Model.
		DefaultTableModel model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.addColumn("Command");
		for (Command command : commands) {
			model.addRow(new Object[] { command });
		}

		// Table.
		JTable table = new JTable();
		table.setModel(model);
		table.setDefaultRenderer(Object.class, new LineWrapCellRenderer());
		table.setTableHeader(null);
		table.setFillsViewportHeight(true);

		// Scroll pane.
		JScrollPane scrollPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
	}

	private static class LineWrapCellRenderer extends JTextArea implements
			TableCellRenderer {

		private static final Color SELECTED_COLOR = new Color(220, 220, 255);
		private int rowHeight;

		public LineWrapCellRenderer() {
			setWrapStyleWord(true);
			setLineWrap(true);
			setBorder(new EmptyBorder(3, 5, 3, 5));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			// Text.
			Command command = (Command) value;
			setText("(" + command.id() + ") " + command.text());

			// Selected?
			if (isSelected) {
				setBackground(SELECTED_COLOR);
			} else {
				setBackground(Color.WHITE);
			}

			// Set the text area width to the table column width.
			int width = table.getColumnModel().getColumn(column).getWidth();
			setSize(new Dimension(width, 1));

			// Get the text area preferred height and add the row margin.
			int height = getPreferredSize().height + table.getRowMargin();

			// Ensure the row height fits the cell with most lines.
			if (column == 0 || height > rowHeight) {
				table.setRowHeight(row, height);
				rowHeight = height;
			}
			return this;
		}
	}
}