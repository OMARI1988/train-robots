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
import java.awt.Stroke;

public class LineNode extends VisualNode {

	private Color color;
	private Stroke stroke;

	public LineNode(float x1, float y1, float x2, float y2) {
		this.x = x1;
		this.y = y1;
		this.width = x2 - x1;
		this.height = y2 - y1;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Stroke getStroke() {
		return stroke;
	}

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

	@Override
	public void render(VisualContext context, float x, float y) {

		// Offset.
		x += this.x;
		y += this.y;

		// Render.
		Graphics2D graphics = context.getGraphics();
		graphics.setPaint(color);
		Stroke previous = graphics.getStroke();
		graphics.setStroke(stroke);
		graphics.drawLine((int) x, (int) y, (int) (x + width),
				(int) (y + height));
		graphics.setStroke(previous);
	}
}
