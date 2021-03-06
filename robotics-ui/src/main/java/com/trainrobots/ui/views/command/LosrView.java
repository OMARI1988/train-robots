/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.command;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.nlp.losr.PartialTree;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.visualization.VisualContext;
import com.trainrobots.ui.visualization.Visualizer;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Visual;
import com.trainrobots.ui.visualization.visuals.VisualTree;

public class LosrView extends JPanel {

	private final CommandService commandService;
	private final Dimension area = new Dimension();
	private final Set<Text> selection = new HashSet<>();
	private Visualizer visualizer;
	private VisualTree visualTree;
	private PartialTree partialTree;
	private Text hover;

	public LosrView(CommandService commandService) {

		// Services.
		this.commandService = commandService;

		// Background.
		setOpaque(true);

		// Mouse.
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent event) {
			}

			public void mousePressed(MouseEvent event) {
				handleMousePressed(event);
			}

			public void mouseReleased(MouseEvent event) {
			}

			public void mouseEntered(MouseEvent event) {
			}

			public void mouseExited(MouseEvent event) {
			}

		});
		addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent event) {
			}

			public void mouseMoved(MouseEvent event) {
				handleMouseMoved(event);
			}
		});

		// Command.
		bind();
	}

	public Items<Text> selection() {
		int size = selection.size();
		if (size == 0) {
			return null;
		}
		Text[] items = new Text[size];
		selection.toArray(items);
		return new ItemsArray<Text>(items);
	}

	public void clearSelection() {
		for (Text item : selection) {
			item.selected(false);
		}
		selection.clear();
		hover = null;
		repaint();
	}

	public PartialTree partialTree() {
		return partialTree;
	}

	public void redrawTree() {

		// Selection.
		selection.clear();
		hover = null;

		// Redraw.
		visualizer = new Visualizer(partialTree);
		repaint();
	}

	public void bind() {

		// Selection.
		selection.clear();
		hover = null;

		// Theme.
		setBackground(commandService.theme().background());

		// Command.
		Command command = commandService.command();
		partialTree = new PartialTree(command);
		visualizer = new Visualizer(partialTree);
		repaint();
	}

	@Override
	public void paintComponent(Graphics graphics) {

		// Paint parent.
		super.paintComponent(graphics);

		// Context.
		VisualContext context = new VisualContext(commandService.theme(),
				(Graphics2D) graphics, getWidth(), getHeight(),
				commandService.boundingBoxes());

		// Build visual tree once if changed.
		if (visualizer != null) {
			visualTree = visualizer.createVisualTree(context);
			visualizer = null;

			// New size.
			int width = 0;
			int height = 0;
			if (visualTree != null) {
				width = (int) visualTree.root().width() + 100;
				height = (int) visualTree.root().height() + 100;
			}

			// Changed size?
			if (area.width != width || area.height != height) {

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

	public Point2D.Float visualToWindow(float vx, float vy) {
		Visual root = visualTree.root();
		float ox = 0.5f * (getWidth() - root.width());
		float oy = 0.5f * (getHeight() - root.height());
		JScrollPane scrollPane = (JScrollPane) getParent().getParent();
		int sx = scrollPane.getHorizontalScrollBar().getValue();
		int sy = scrollPane.getVerticalScrollBar().getValue();
		return new Point2D.Float(vx + ox - sx, vy + oy - sy);
	}

	private Point2D.Float mouseToVisual(float mx, float my) {
		Visual root = visualTree.root();
		float ox = 0.5f * (getWidth() - root.width());
		float oy = 0.5f * (getHeight() - root.height());
		return new Point2D.Float(mx - ox, my - oy);
	}

	private void handleMouseMoved(MouseEvent event) {

		// No tree?
		if (visualTree == null) {
			return;
		}

		// Find element.
		Point2D.Float p = mouseToVisual(event.getX(), event.getY());
		Text text = visualTree.find(Text.class, p.x, p.y);

		// No change?
		if (Objects.equals(text, hover)) {
			return;
		}

		// Clear previous.
		if (hover != null && !selection.contains(hover)) {
			hover.selected(false);
		}

		// New hover.
		hover = text;
		if (hover != null) {
			hover.selected(true);
		}

		// Repaint.
		repaint();
	}

	private void handleMousePressed(MouseEvent event) {

		// Recalculate hover.
		handleMouseMoved(event);

		// Clear.
		if (hover == null) {
			clearSelection();
			return;
		}

		// Already selected?
		if (selection.contains(hover)) {
			return;
		}

		// Add.
		if (event.isShiftDown() || event.isControlDown()) {
			selection.add(hover);
			repaint();
			return;
		}

		// Single item.
		for (Text item : selection) {
			item.selected(false);
		}
		selection.clear();
		selection.add(hover);
		repaint();
	}
}