/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.command;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;

import com.trainrobots.losr.Losr;
import com.trainrobots.nlp.losr.PartialTree;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandAware;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.views.PaneView;
import com.trainrobots.ui.visualization.visuals.Text;

public class CommandView extends PaneView implements CommandAware {

	private final CommandService commandService;
	private final LosrView losrView;
	private final Editor editor;
	private final JLayeredPane layeredPane;
	private Component popup;

	public CommandView(CommandService commandService,
			WindowService windowService, TreebankService treebankService) {
		super(title(commandService.command()));

		// View.
		this.commandService = commandService;
		setLayout(new BorderLayout());

		// Scroll pane.
		losrView = new LosrView(commandService);
		JScrollPane scrollPane = new JScrollPane(losrView);
		AdjustmentListener adjustmentListener = new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent event) {
				closePopup();
			}
		};
		scrollPane.getHorizontalScrollBar().addAdjustmentListener(
				adjustmentListener);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(
				adjustmentListener);

		// Layered pane.
		layeredPane = new JLayeredPane();
		layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
		layeredPane.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent event) {
				scrollPane.setBounds(0, 0, layeredPane.getWidth(),
						layeredPane.getHeight());
			}

			public void componentMoved(ComponentEvent event) {
			}

			public void componentShown(ComponentEvent event) {
			}

			public void componentHidden(ComponentEvent event) {
			}
		});
		add(layeredPane, BorderLayout.CENTER);

		// Editor.
		editor = new Editor(windowService, treebankService, losrView,
				this::showPopup);

		// Keys.
		bindKey("ESCAPE", this::handleEscape);
	}

	public PartialTree partialTree() {
		return losrView.partialTree();
	}

	public Editor editor() {
		return editor;
	}

	@Override
	public String paneType() {
		return "command";
	}

	@Override
	public boolean alwaysBehind() {
		return true;
	}

	@Override
	public void bindTo(Command command) {
		setTitle(title(command));
		losrView.bind();
	}

	public void redraw() {
		losrView.repaint();
	}

	private void handleEscape() {
		if (popup != null) {
			closePopup();
		} else {
			losrView.clearSelection();
		}
	}

	private void closePopup() {
		if (popup != null) {
			layeredPane.remove(popup);
			layeredPane.revalidate();
			losrView.clearSelection();
			popup = null;
		}
	}

	private static String title(Command command) {
		return "Command " + command.id();
	}

	private void showPopup(Text text, Losr losr, Object[] options,
			Object selected) {

		// Already visible?
		if (popup != null) {
			return;
		}

		// Create popup.
		JComboBox comboBox = new JComboBox(options) {
			public void processKeyEvent(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
					closePopup();
				} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					closePopup();
					CommandView.this.editor.acceptChange(losr,
							((JComboBox) event.getComponent())
									.getSelectedItem());
				} else {
					super.processKeyEvent(event);
				}
			}
		};
		comboBox.setFont(commandService.theme().font());
		comboBox.setSelectedItem(selected);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					closePopup();
					editor.acceptChange(losr, event.getItem());
				}
			}
		});

		// Show.
		showPopup(text, comboBox);
	}

	private void showPopup(Text text, Component component) {

		// Bounds.
		Point2D.Float p = losrView.visualToWindow(text.x(), text.y());
		Dimension size = component.getPreferredSize();
		int x = (int) p.x;
		int y = (int) p.y;
		int width = size.width;
		int height = size.height;
		component.setBounds(x - 2, y, width, height);

		// Add.
		layeredPane.add(component, JLayeredPane.POPUP_LAYER);
		layeredPane.revalidate();
		popup = component;
	}
}