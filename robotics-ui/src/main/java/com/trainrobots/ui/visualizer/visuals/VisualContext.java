/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.visuals;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class VisualContext {

	private final Graphics2D graphics;
	private final int width;
	private final int height;
	private final boolean isBoundingBoxes;

	private static final VisualContext defaultContext;

	static {
		BufferedImage image = new BufferedImage(10, 10,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.createGraphics();
		defaultContext = new VisualContext(graphics, 10, 10, false);
	}

	public static VisualContext defaultContext() {
		return defaultContext;
	}

	public VisualContext(Graphics2D graphics, int width, int height,
			boolean isBoundingBoxes) {

		// Initiate.
		this.graphics = graphics;
		this.width = width;
		this.height = height;
		this.isBoundingBoxes = isBoundingBoxes;

		// Anti-aliasing.
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Fractional metrics.
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}

	public Graphics2D getGraphics() {
		return graphics;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isBoundingBoxes() {
		return isBoundingBoxes;
	}
}