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

package com.trainrobots.ui.graphics;

import javax.media.opengl.GL2;

public class Renderer {

	public static void setup(GL2 gl, int width, int height) {

		// Set viewing projection.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glFrustum(-0.5f, 0.5f, -0.5f, 0.5f, 1.0f, 3.0f);

		// Position viewer.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glTranslatef(0.0f, 0.0f, -2.0f);

		// Position object.
		gl.glRotatef(30.0f, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(30.0f, 0.0f, 1.0f, 0.0f);

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
	}

	public static void render(GL2 gl, int width, int height) {

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glBegin(GL2.GL_QUADS);
		gl.glNormal3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(0.5f, 0.5f, 0.5f);
		gl.glVertex3f(-0.5f, 0.5f, 0.5f);
		gl.glVertex3f(-0.5f, -0.5f, 0.5f);
		gl.glVertex3f(0.5f, -0.5f, 0.5f);

		gl.glNormal3f(0.0f, 0.0f, -1.0f);
		gl.glVertex3f(-0.5f, -0.5f, -0.5f);
		gl.glVertex3f(-0.5f, 0.5f, -0.5f);
		gl.glVertex3f(0.5f, 0.5f, -0.5f);
		gl.glVertex3f(0.5f, -0.5f, -0.5f);

		gl.glNormal3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(0.5f, 0.5f, 0.5f);
		gl.glVertex3f(0.5f, 0.5f, -0.5f);
		gl.glVertex3f(-0.5f, 0.5f, -0.5f);
		gl.glVertex3f(-0.5f, 0.5f, 0.5f);

		gl.glNormal3f(0.0f, -1.0f, 0.0f);
		gl.glVertex3f(-0.5f, -0.5f, -0.5f);
		gl.glVertex3f(0.5f, -0.5f, -0.5f);
		gl.glVertex3f(0.5f, -0.5f, 0.5f);
		gl.glVertex3f(-0.5f, -0.5f, 0.5f);

		gl.glNormal3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.5f, 0.5f, 0.5f);
		gl.glVertex3f(0.5f, -0.5f, 0.5f);
		gl.glVertex3f(0.5f, -0.5f, -0.5f);
		gl.glVertex3f(0.5f, 0.5f, -0.5f);

		gl.glNormal3f(-1.0f, 0.0f, 0.0f);
		gl.glVertex3f(-0.5f, -0.5f, -0.5f);
		gl.glVertex3f(-0.5f, -0.5f, 0.5f);
		gl.glVertex3f(-0.5f, 0.5f, 0.5f);
		gl.glVertex3f(-0.5f, 0.5f, -0.5f);
		gl.glEnd();
	}
}