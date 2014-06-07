/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.visuals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class VisualTree {

	private final VisualNode root;

	public VisualTree(VisualNode root) {
		this.root = root;
	}

	public VisualNode getRoot() {
		return root;
	}

	public void render(VisualContext context) {

		// Center the node.
		float x = 0.5f * (context.getWidth() - root.getWidth());
		float y = 0.5f * (context.getHeight() - root.getHeight());

		// Render.
		renderNode(context, x, y, root);
	}

	private void renderNode(VisualContext context, float x, float y,
			VisualNode node) {

		// Render node.
		node.render(context, x, y);

		// Adjust origin.
		x += node.getX();
		y += node.getY();

		// Bounding box.
		if (context.isBoundingBoxes() && !(node instanceof LineNode)) {
			Graphics2D graphics = context.getGraphics();
			graphics.setPaint(Color.BLUE);
			graphics.draw(new Rectangle2D.Float(x, y, node.getWidth(), node
					.getHeight()));
		}

		// Render children.
		int size = node.getChildCount();
		for (int i = 0; i < size; i++) {
			renderNode(context, x, y, node.getChild(i));
		}
	}
}