/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JPanel;

import com.jogamp.opengl.util.FPSAnimator;
import com.trainrobots.scenes.Scene;
import com.trainrobots.ui.renderer.Renderer;
import com.trainrobots.ui.renderer.scene.SceneElement;

public class SceneView extends JPanel {

	private int mouseX;
	private int mouseY;
	private int width;
	private int height;

	public SceneView(Scene scene) {

		// Scene.
		SceneElement sceneElement = new SceneElement(scene);
		Renderer renderer = new Renderer(sceneElement);

		// Select a multisample capability with 8 samples per pixel.
		GLCapabilities caps = new GLCapabilities(null);
		caps.setSampleBuffers(true);
		caps.setNumSamples(8);

		// Canvas.
		GLJPanel glPanel = new GLJPanel(caps);
		glPanel.addGLEventListener(new GLEventListener() {

			public void display(GLAutoDrawable drawable) {
				renderer.display(drawable.getGL().getGL2());
			}

			public void dispose(GLAutoDrawable drawable) {
			}

			public void init(GLAutoDrawable drawable) {
				renderer.initiate(drawable.getGL().getGL2());
			}

			public void reshape(GLAutoDrawable drawable, int x, int y, int w,
					int h) {
				width = w;
				height = h;
				renderer.reshape(drawable.getGL().getGL2(), width, height);
			}
		});
		glPanel.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {

				// Rotate the view.
				float dx = 180 * (e.getY() - mouseY) / (float) height;
				float dy = 180 * (e.getX() - mouseX) / (float) width;
				sceneElement.incrementRotation(dx, dy);
				mouseX = e.getX();
				mouseY = e.getY();
			}

			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		setLayout(new BorderLayout());
		add(glPanel, BorderLayout.CENTER);

		// Start redraw thread.
		FPSAnimator redrawThread = new FPSAnimator(glPanel, 60);
		redrawThread.start();
	}
}