/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import com.trainrobots.ui.visualization.VisualContext;

public abstract class Text extends Visual {

	private final String text;
	private final Font font;
	private final Color color;
	private float textOffsetX;
	private float textOffsetY;
	private boolean selected;

	protected Text(VisualContext context, String text, Font font, Color color) {

		// Text.
		this.text = text;
		this.font = font;
		this.color = color;

		// Bounds.
		FontRenderContext fontRenderContext = context.graphics()
				.getFontRenderContext();
		Rectangle2D.Float bounds = (Rectangle2D.Float) font.getStringBounds(
				text, fontRenderContext);
		textOffsetX = bounds.x;
		textOffsetY = bounds.y;
		width = bounds.width;
		height = bounds.height;
	}

	public String text() {
		return text;
	}

	public Font font() {
		return font;
	}

	public Color color() {
		return color;
	}

	@Override
	public String toString() {
		return text;
	}

	public float textOffsetY() {
		return textOffsetY;
	}

	public boolean selected() {
		return selected;
	}

	public void selected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public void render(VisualContext context, float x, float y) {

		// Position.
		x += this.x;
		y += this.y;

		// Selected?
		Graphics2D graphics = context.graphics();
		if (selected) {
			graphics.setPaint(context.theme().selected());
			graphics.fill(new Rectangle2D.Float(x - 2, y, width + 4, height));
		}

		// Draw text.
		graphics.setFont(font);
		graphics.setColor(color);
		graphics.drawString(text, x - textOffsetX, y - textOffsetY);
	}
}