/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.trainrobots.RoboticException;
import com.trainrobots.ui.GraphicsRenderer;
import com.trainrobots.ui.visualizer.losr.LosrTree;
import com.trainrobots.ui.visualizer.visuals.VisualContext;
import com.trainrobots.ui.visualizer.visuals.VisualNode;
import com.trainrobots.ui.visualizer.visuals.VisualTree;

public class PngWriter implements GraphicsRenderer {

	private final LosrTree tree;
	private final boolean darkTheme;

	public PngWriter(LosrTree tree) {
		this(tree, false);
	}

	public PngWriter(LosrTree tree, boolean darkTheme) {
		this.tree = tree;
		this.darkTheme = darkTheme;
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
		Visualizer visualizer = new Visualizer(tree, darkTheme);
		VisualContext visualContext = VisualContext.defaultContext();
		VisualTree visualTree = visualizer.createVisualTree(visualContext);
		VisualNode root = visualTree.getRoot();

		// Image.
		int width = (int) (root.getWidth() + 4);
		int height = (int) (root.getHeight() + 4);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		// Render.
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(darkTheme ? new Color(33, 33, 33) : Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		visualTree.render(new VisualContext(graphics, width, height, false));
		graphics.dispose();
		return image;
	}
}