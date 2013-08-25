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

public class Renderer {

	public static void setup(GL2 gl2, int width, int height) {

		// TODO: INITIATE VIEW PORT!

		// gl2.glMatrixMode(GL2.GL_PROJECTION);
		// gl2.glLoadIdentity();
		//
		// GLU glu = new GLU();
		// glu.gluOrtho2D(0.0f, width, 0.0f, height);
		//
		// gl2.glMatrixMode(GL2.GL_MODELVIEW);
		// gl2.glLoadIdentity();
		//
		// gl2.glViewport(0, 0, width, height);
	}

	public static void render(GL2 gl, int width, int height) {
		float x, y;
		int steps = 8;
		double size = 0.90 / steps;

		gl.glColor3f(1.f, 1.f, 1.f);

		for (int i = 0; i < steps; i++) {
			for (int j = 0; j < steps; j++) {
				gl.glBegin(GL2.GL_QUADS);
				x = 0.03f + 0.90f * (float) j / steps;
				y = 0.03f + 0.90f * (float) i / steps;
				if ((i + j) % 2 == 0) {
					gl.glColor3f(0.0f, 0f, 0f);
					gl.glVertex2d(x, y);
					gl.glVertex2d(x, y + size);
					gl.glVertex2d(x + size, y + size);
					gl.glVertex2d(x + size, y);

				} else {
					gl.glColor3f(1, 1, 1);
					gl.glVertex2d(x, y);
					gl.glVertex2d(x + size, y);
					gl.glVertex2d(x + size, y + size);
					gl.glVertex2d(x, y + size);
				}
				gl.glEnd();

			}
		}

		gl.glLineWidth(5);

		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glColor3f(1.f, 1.f, 1.f);
		gl.glVertex2d(0.0255, 0.0255);

		gl.glColor3f(0.f, 0.f, 0.f);
		gl.glVertex2d(.93, .0255);

		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex2d(.930, .930);

		gl.glColor3f(0.f, 0.f, 0.f);
		gl.glVertex2d(0.0255, .930);
		gl.glEnd();
	}
}