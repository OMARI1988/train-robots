/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.visuals;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

public class TextNode extends VisualNode {

	private final String text;
	private final Font font;
	private final AttributedString attributedText;
	private float textOffsetX;
	private float textOffsetY;
	private Color color;
	private Color background;

	public TextNode(VisualContext context, String text, Font font) {

		// Initiate.
		this.text = text;
		this.font = font;

		// Attributed text.
		attributedText = new AttributedString(text);
		attributedText.addAttribute(TextAttribute.FONT, font);

		FontRenderContext fontRenderContext = context.getGraphics()
				.getFontRenderContext();
		Rectangle2D.Float bounds;

		// Measure the string.
		bounds = (Rectangle2D.Float) font.getStringBounds(text,
				fontRenderContext);

		// Set bounds.
		textOffsetX = bounds.x;
		textOffsetY = bounds.y;
		width = bounds.width;
		height = bounds.height;
	}

	public Font getFont() {
		return font;
	}

	public void setColor(Color color) {
		attributedText.addAttribute(TextAttribute.FOREGROUND, color);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(int start, int end, Color color) {
		attributedText
				.addAttribute(TextAttribute.FOREGROUND, color, start, end);
	}

	public void setFont(int start, int end, Font font) {
		attributedText.addAttribute(TextAttribute.FONT, font, start, end);
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public String getText() {
		return text;
	}

	public AttributedString getAttributedText() {
		return attributedText;
	}

	public void setTextOffsetX(float textOffsetX) {
		this.textOffsetX = textOffsetX;
	}

	public float getTextOffsetX() {
		return textOffsetX;
	}

	public void setTextOffsetY(float textOffsetY) {
		this.textOffsetY = textOffsetY;
	}

	public float getTextOffsetY() {
		return textOffsetY;
	}

	@Override
	public void render(VisualContext context, float x, float y) {

		// Position.
		x += this.x;
		y += this.y;

		// Background.
		Graphics2D graphics = context.getGraphics();
		if (background != null) {
			graphics.setPaint(background);
			graphics.fill(new Rectangle2D.Float(x - 3, y, width + 6, height));
		}

		// Draw text.
		graphics.drawString(attributedText.getIterator(), x - textOffsetX, y
				- textOffsetY);
	}
}