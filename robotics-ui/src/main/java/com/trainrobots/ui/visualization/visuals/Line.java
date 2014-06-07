/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.trainrobots.ui.visualization.VisualContext;

public class Line extends Visual {

	private final Color color;
	private final Stroke stroke;

	public Line(float x1, float y1, float x2, float y2, Color color,
			Stroke stroke) {
		this.x = x1;
		this.y = y1;
		this.width = x2 - x1;
		this.height = y2 - y1;
		this.color = color;
		this.stroke = stroke;
	}

	public Stroke stroke() {
		return stroke;
	}

	@Override
	public void render(VisualContext context, float x, float y) {

		// Offset.
		x += this.x;
		y += this.y;

		// Render.
		Graphics2D graphics = context.graphics();
		graphics.setPaint(color);
		Stroke previous = graphics.getStroke();
		graphics.setStroke(stroke);
		graphics.drawLine((int) x, (int) y, (int) (x + width),
				(int) (y + height));
		graphics.setStroke(previous);
	}
}
