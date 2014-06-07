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
import java.awt.geom.Path2D;

public class ArrowNode extends VisualNode {

	private Color color;
	private boolean up;

	public ArrowNode(boolean up, float x, float y, float width, float height) {
		this.up = up;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public void render(VisualContext context, float x, float y) {

		// Offset.
		x += this.x;
		y += this.y;

		// Path.
		Path2D path = new Path2D.Float();
		path.moveTo(x, up ? y + height : y);
		path.lineTo(x + width, up ? y + height : y);
		path.lineTo(x + 0.5f * width, up ? y : y + height);

		// Render.
		Graphics2D graphics = context.getGraphics();
		graphics.setPaint(color);
		graphics.fill(path);
	}
}
