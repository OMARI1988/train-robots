/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLOffscreenAutoDrawable;
import javax.media.opengl.GLProfile;

import com.trainrobots.RoboticException;
import com.trainrobots.ui.GraphicsRenderer;
import com.trainrobots.ui.renderer.scene.LayoutElement;

public class Buffer implements GraphicsRenderer, AutoCloseable {

	private final Renderer renderer;
	private final GLOffscreenAutoDrawable buffer;
	private final GLContext context;
	private final int width;
	private final int height;

	public Buffer(LayoutElement layout, int width, int height) {

		// Renderer.
		this.renderer = new Renderer(layout);
		this.width = width;
		this.height = height;

		// Buffer.
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(false);
		caps.setPBuffer(true);
		caps.setNumSamples(8);
		caps.setSampleBuffers(true);
		GLDrawableFactory factory = GLDrawableFactory.getFactory(caps
				.getGLProfile());
		buffer = (GLOffscreenAutoDrawable) factory.createOffscreenAutoDrawable(
				null, caps, null, width, height, null);
		buffer.display();

		// Context.
		context = buffer.getContext();

		// Initiate and set viewport.
		renderer.initiate(makeCurrent());
		renderer.reshape(makeCurrent(), width, height);
	}

	public GL2 makeCurrent() {
		context.makeCurrent();
		return context.getGL().getGL2();
	}

	@SuppressWarnings("deprecation")
	public BufferedImage renderToImage() {
		renderer.display(makeCurrent());
		return com.jogamp.opengl.util.awt.Screenshot.readToBufferedImage(width,
				height);
	}

	public void renderToFile(String filename) {
		try {
			BufferedImage img = renderToImage();
			File outputfile = new File(filename);
			ImageIO.write(img, "png", outputfile);
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	public byte[] renderToArray() {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageIO.write(renderToImage(), "png", stream);
			return stream.toByteArray();
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	@Override
	public void close() {
		buffer.destroy();
	}
}
