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
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

import com.trainrobots.ui.visualization.VisualContext;

public class Text extends Visual {

	private final String text;
	private final Font font;
	private final Color color;
	private final AttributedString attributedText;
	private float textOffsetX;
	private float textOffsetY;

	public Text(VisualContext context, String text, Font font, Color color) {

		// Text.
		this.text = text;
		attributedText = new AttributedString(text);

		// Font.
		this.font = font;
		attributedText.addAttribute(TextAttribute.FONT, font);

		// Color.
		this.color = color;
		attributedText.addAttribute(TextAttribute.FOREGROUND, color);

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

	@Override
	public void render(VisualContext context, float x, float y) {

		// Position.
		x += this.x;
		y += this.y;

		// Draw text.
		Graphics2D graphics = context.graphics();
		graphics.drawString(attributedText.getIterator(), x - textOffsetX, y
				- textOffsetY);
	}
}