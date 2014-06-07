/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.trainrobots.ui.visualization.visuals.Line;
import com.trainrobots.ui.visualization.visuals.Visual;

public class VisualTree {

	private final Visual root;

	public VisualTree(Visual root) {
		this.root = root;
	}

	public Visual root() {
		return root;
	}

	public void render(VisualContext context) {

		// Center.
		float x = 0.5f * (context.width() - root.width());
		float y = 0.5f * (context.height() - root.height());

		// Render.
		renderVisual(context, x, y, root);
	}

	private void renderVisual(VisualContext context, float x, float y,
			Visual visual) {

		// Render.
		visual.render(context, x, y);

		// Adjust origin.
		x += visual.x();
		y += visual.y();

		// Bounding box.
		if (context.boundingBoxes() && !(visual instanceof Line)) {
			Graphics2D graphics = context.graphics();
			graphics.setPaint(context.theme().boundingBox());
			graphics.draw(new Rectangle2D.Float(x, y, visual.width(), visual
					.height()));
		}

		// Render children.
		int size = visual.count();
		for (int i = 0; i < size; i++) {
			renderVisual(context, x, y, visual.get(i));
		}
	}
}