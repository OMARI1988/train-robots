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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Renderer {

	public static void setup(GL2 gl2, int width, int height) {

		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glLoadIdentity();

		GLU glu = new GLU();
		glu.gluOrtho2D(0.0f, width, 0.0f, height);

		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glLoadIdentity();

		gl2.glViewport(0, 0, width, height);
	}

	public static void render(GL2 gl2, int width, int height) {
		gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl2.glLoadIdentity();
		gl2.glBegin(GL.GL_TRIANGLES);
		gl2.glColor3f(1, 0, 0);
		gl2.glVertex2f(0, 0);
		gl2.glColor3f(0, 1, 0);
		gl2.glVertex2f(width, 0);
		gl2.glColor3f(0, 0, 1);
		gl2.glVertex2f(width / 2, height);
		gl2.glEnd();
	}
}