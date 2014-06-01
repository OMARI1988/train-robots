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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.trainrobots.RoboticSystem;
import com.trainrobots.collections.Items;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.Styles;
import com.trainrobots.ui.services.data.DataService;

public class CommandsView extends PaneView {

	public CommandsView(DataService dataService) {
		super("Commands");

		// Initiate.
		setSize(400, 550);
		setLayout(new BorderLayout());

		// Commands.
		Scene scene = dataService.selectedScene();
		RoboticSystem roboticSystem = dataService.system();
		Items<Command> commands = roboticSystem.commands().forScene(scene);

		// Model.
		TableModel model = new AbstractTableModel() {

			public int getRowCount() {
				return commands.count();
			}

			public int getColumnCount() {
				return 1;
			}

			public Class<?> getColumnClass(int columnIndex) {
				return Command.class;
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				return commands.get(rowIndex);
			}
		};

		// Table.
		JTable table = new JTable(model);
		table.setDefaultRenderer(Command.class, new CommandRenderer());
		table.setTableHeader(null);
		table.setFillsViewportHeight(true);
		table.setRowSelectionInterval(0, 0);

		// Scroll pane.
		JScrollPane scrollPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public String paneType() {
		return "commands";
	}

	private static class CommandRenderer extends JPanel implements
			TableCellRenderer {

		private final JLabel label = new JLabel();
		private final JTextArea textArea = new JTextArea();
		private int rowHeight;

		public CommandRenderer() {

			// Label.
			setLayout(new BorderLayout());
			add(label, BorderLayout.WEST);
			label.setPreferredSize(new Dimension(50, 0));
			label.setVerticalAlignment(SwingConstants.TOP);

			// Text area.
			textArea.setBackground(new Color(0, 0, 0, 0));
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
			textArea.setBorder(new EmptyBorder(0, 0, 0, 0));
			add(textArea, BorderLayout.CENTER);

			// Component.
			setBorder(new EmptyBorder(3, 5, 5, 5));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			// Text.
			Command command = (Command) value;
			label.setText(Integer.toString(command.id()));
			textArea.setText(command.text());

			// Selected?
			if (isSelected) {
				label.setForeground(Color.WHITE);
				textArea.setForeground(Color.WHITE);
				setBackground(Styles.SELECTED_COLOR);
			} else {
				label.setForeground(Color.BLUE);
				textArea.setForeground(Color.BLACK);
				setBackground(row % 2 == 0 ? Color.WHITE
						: Styles.ALTERNATE_COLOR);
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