/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer;

import javax.media.opengl.GL2;

import com.trainrobots.ui.renderer.scene.LayoutElement;

public class Renderer {

	private final LayoutElement layout;

	public Renderer(LayoutElement layout) {
		this.layout = layout;
	}

	public void display(GL2 gl) {

		// Clear.
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

		// Model/view transform.
		gl.glLoadIdentity();
		gl.glTranslated(-10.0, -7.0, 0.0);

		// Set up lighting.
		float[] pos0 = { -1.0f, 0.0f, -1.0f, 0.0f }, pos1 = { 0.0f, 0.0f,
				-1.0f, 0.0f };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos0, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, pos1, 0);

		// Render.
		layout.render(gl);
		gl.glPopAttrib();
	}

	public void initiate(GL2 gl) {

		// Clear.
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_CULL_FACE);

		// Lighting.
		float[] diff0 = { 0.6f, 0.6f, 0.6f, 1.0f };
		float[] spec0 = { 1.0f, 1.0f, 1.0f, 1.0f };
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diff0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spec0, 0);

		// Lighting.
		float[] diff1 = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] spec1 = { 1.0f, 1.0f, 1.0f, 1.0f };
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diff1, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, spec1, 0);
	}

	public void reshape(GL2 gl, int width, int height) {

		// Set viewport to maintain a correct orthographic projection.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		double h = 37.0f;
		double w = 37.0f;
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