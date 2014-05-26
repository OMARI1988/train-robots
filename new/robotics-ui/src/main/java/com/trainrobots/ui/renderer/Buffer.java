/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer;

import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.*;

import com.jogamp.opengl.util.awt.Screenshot;
import com.trainrobots.RoboticException;
import com.trainrobots.ui.renderer.scene.SceneElement;

@SuppressWarnings("deprecation")
public class Buffer extends Renderer {

	private GLOffscreenAutoDrawable m_buffer; // pbuffer
	private GLContext m_context; // opengl context

	public Buffer(SceneElement scene, int w, int h) {
		super(scene, w, h);

		// Construct pbuffer.
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(false);
		caps.setPBuffer(true);
		caps.setNumSamples(8);
		caps.setSampleBuffers(true);
		GLDrawableFactory factory = GLDrawableFactory.getFactory(caps
				.getGLProfile());
		m_buffer = (GLOffscreenAutoDrawable) factory
				.createOffscreenAutoDrawable(null, caps, null, m_width,
						m_height, null);
		m_buffer.display();

		m_context = m_buffer.getContext();

		// Initiate and set viewport.
		initiate(makeCurrent());
		reshape(makeCurrent(), m_width, m_height);
	}

	public GL2 makeCurrent() {
		m_context.makeCurrent();
		return m_context.getGL().getGL2();
	}

	// render to a buffered image
	public BufferedImage renderToImage() {
		display(makeCurrent());
		return Screenshot.readToBufferedImage(m_width, m_height);
	}

	// renders to a buffered image and saves image to file
	public void renderToFile(String fn) {
		try {
			BufferedImage img = renderToImage();
			File outputfile = new File(fn);
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public byte[] renderToArray() {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageIO.write(renderToImage(), "png", stream);
			return stream.toByteArray();
		} catch (IOException e) {
			throw new RoboticException(e);
		}
	}

	public void destroy() {
		m_buffer.destroy();
	}
}
