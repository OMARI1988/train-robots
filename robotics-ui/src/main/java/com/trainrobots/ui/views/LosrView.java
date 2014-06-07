/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JViewport;

import com.trainrobots.losr.Losr;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.visualizer.Visualizer;
import com.trainrobots.ui.visualizer.losr.LosrTree;
import com.trainrobots.ui.visualizer.visuals.VisualContext;
import com.trainrobots.ui.visualizer.visuals.VisualTree;

public class LosrView extends JPanel {

	private Visualizer visualizer;
	private VisualTree visualTree;
	private final CommandService commandService;
	private final Dimension area = new Dimension();

	public LosrView(CommandService commandService) {
		this.commandService = commandService;
		setOpaque(true);
		bind();
	}

	public void bind() {

		// Background.
		if (commandService.darkTheme()) {
			setBackground(new Color(33, 33, 33));
		} else {
			setBackground(Color.WHITE);
		}

		// Command.
		Command command = commandService.command();
		Losr losr = command.losr();
		if (losr != null) {
			visualizer = new Visualizer(new LosrTree(command),
					commandService.darkTheme());
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics graphics) {

		// Paint parent.
		super.paintComponent(graphics);

		// Context.
		VisualContext context = new VisualContext((Graphics2D) graphics,
				getWidth(), getHeight(), commandService.boundingBoxes());

		// New visual tree?
		if (visualizer != null) {
			visualTree = visualizer.createVisualTree(context);
			visualizer = null;

			// New size.
			int width = 0;
			int height = 0;
			if (visualTree != null) {
				width = (int) visualTree.getRoot().getWidth() + 100;
				height = (int) visualTree.getRoot().getHeight() + 100;
			}

			// Changed size?
			if (area.getWidth() != width || area.getHeight() != height) {

				// Area has changed.
				area.setSize(width, height);
				setPreferredSize(area);

				// Update scrollbars.
				revalidate();

				// Left align.
				JViewport viewPort = (JViewport) getParent();
				viewPort.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
			}
		}

		// Render.
		if (visualTree != null) {
			visualTree.render(context);
		}
	}
}