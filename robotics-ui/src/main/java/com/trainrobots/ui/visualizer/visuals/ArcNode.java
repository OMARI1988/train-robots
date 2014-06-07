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
import java.awt.geom.Arc2D;

public class ArcNode extends VisualNode {

	private Color color;

	public ArcNode(float x, float y, float width, float height) {
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

		Graphics2D graphics = context.getGraphics();
		graphics.setPaint(color);

		x += this.x;
		y += this.y;

		Arc2D shape = new Arc2D.Float(x - width, y, width * 2, height, -90, 180,
				Arc2D.OPEN);
		graphics.draw(shape);
	}
}