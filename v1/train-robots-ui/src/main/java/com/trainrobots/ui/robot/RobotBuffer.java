/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.ui.robot;

import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.*;

import com.jogamp.opengl.util.awt.Screenshot;
import com.trainrobots.core.CoreException;

@SuppressWarnings("deprecation")
public class RobotBuffer extends RobotRenderer {

	private GLOffscreenAutoDrawable m_buffer; // pbuffer
	private GLContext m_context; // opengl context

	public RobotBuffer(RobotControl ctrl, int w, int h) {
		super(ctrl, w, h);

		// construct pbuffer
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

		// init and set viewport
		init(makeCurrent());
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
			throw new CoreException(e);
		}
	}

	public void destroy() {
		m_buffer.destroy();
	}

}
