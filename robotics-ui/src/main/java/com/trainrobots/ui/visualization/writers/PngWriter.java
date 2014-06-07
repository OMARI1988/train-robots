/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.writers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.trainrobots.RoboticException;
import com.trainrobots.ui.GraphicsRenderer;
import com.trainrobots.ui.visualization.VisualContext;
import com.trainrobots.ui.visualization.VisualTree;
import com.trainrobots.ui.visualization.Visualizer;
import com.trainrobots.ui.visualization.losr.LosrTree;
import com.trainrobots.ui.visualization.themes.Theme;
import com.trainrobots.ui.visualization.themes.Themes;
import com.trainrobots.ui.visualization.visuals.Visual;

public class PngWriter implements GraphicsRenderer {

	private final LosrTree tree;
	private final Theme theme;

	public PngWriter(LosrTree tree) {
		this(tree, Themes.Simple);
	}

	public PngWriter(LosrTree tree, Theme theme) {
		this.tree = tree;
		this.theme = theme;
	}

	public void renderToFile(String filename) {
		try {
			ImageIO.write(image(), "png", new File(filename));
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	public byte[] renderToArray() {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image(), "png", output);
			return output.toByteArray();
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	private BufferedImage image() {

		// Visual tree.
		Visualizer visualizer = new Visualizer(tree, theme);
		VisualContext visualContext = VisualContext.defaultContext();
		VisualTree visualTree = visualizer.createVisualTree(visualContext);
		Visual root = visualTree.root();

		// Image.
		int width = (int) (root.width() + 4);
		int height = (int) (root.height() + 4);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		// Render.
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(theme.background());
		graphics.fillRect(0, 0, width, height);
		visualTree.render(new VisualContext(graphics, width, height, false));
		graphics.dispose();
		return image;
	}
}