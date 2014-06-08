/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.trainrobots.ui.visualization.themes.Theme;

public class VisualContext {

	private final Theme theme;
	private final Graphics2D graphics;
	private final int width;
	private final int height;
	private final boolean boundingBoxes;

	public VisualContext(Theme theme, Graphics2D graphics, int width,
			int height, boolean boundingBoxes) {

		// Initiate.
		this.theme = theme;
		this.graphics = graphics;
		this.width = width;
		this.height = height;
		this.boundingBoxes = boundingBoxes;

		// Anti-aliasing.
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Fractional metrics.
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}

	public static VisualContext of(Theme theme) {
		BufferedImage image = new BufferedImage(10, 10,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.createGraphics();
		return new VisualContext(theme, graphics, 10, 10, false);
	}

	public Theme theme() {
		return theme;
	}

	public Graphics2D graphics() {
		return graphics;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public boolean boundingBoxes() {
		return boundingBoxes;
	}
}