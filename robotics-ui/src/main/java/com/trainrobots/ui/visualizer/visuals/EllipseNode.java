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
import java.awt.geom.Ellipse2D;

public class EllipseNode extends VisualNode {

	private Color color;

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
		graphics.fill(new Ellipse2D.Float(this.x + x, this.y + y, width, height));
	}
}