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

import javax.media.opengl.GL2;

public class RobotRenderer {
	protected RobotControl m_rbtctrl;
	protected float m_rotx = 37.0f, // rotation about the x axis
			m_roty = -42.5f; // rotation about the y axis
	protected int m_width, m_height;

	public RobotRenderer(RobotControl ctrl, int w, int h) {
		m_rbtctrl = ctrl;
		m_width = w;
		m_height = h;
	}

	public void display(GL2 gl) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

		// modelview transform
		gl.glLoadIdentity();
		gl.glTranslated(-10.0, -7.0, 0.0);

		// set up lighting
		float[] pos0 = { -1.0f, 0.0f, -1.0f, 0.0f }, pos1 = { 0.0f, 0.0f,
				-1.0f, 0.0f };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos0, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, pos1, 0);

		gl.glRotatef(m_rotx, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(m_roty, 0.0f, 1.0f, 0.0f);

		// render the scene
		m_rbtctrl.render(gl);

		gl.glPopAttrib();
	}

	public void init(GL2 gl) {
		System.out.println("init: " + gl);

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_CULL_FACE);

		// init lights
		float[] diff0 = { 0.6f, 0.6f, 0.6f, 1.0f };
		float[] spec0 = { 1.0f, 1.0f, 1.0f, 1.0f };
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diff0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spec0, 0);

		float[] diff1 = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] spec1 = { 1.0f, 1.0f, 1.0f, 1.0f };
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diff1, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, spec1, 0);
	}

	public void reshape(GL2 gl, int width, int height) {
		System.out.println("reshape: " + gl);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		double h = 37.0f, w = 37.0f;
		if (width > height) {
			w = 37.0f * ((double) width) / height;
		} else {
			h = 37.0f * ((double) height) / width;
		}

		gl.glOrtho(-w, w, -h, h, -100.0f, 100.0f);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glViewport(0, 0, width, height);
	}

}
