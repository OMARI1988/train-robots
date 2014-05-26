/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.demo;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.trainrobots.scenes.Scene;
import com.trainrobots.ui.renderer.Renderer;
import com.trainrobots.ui.renderer.scene.SceneElement;

public class DemoWindow extends JFrame implements GLEventListener,
		MouseMotionListener, WindowListener {

	private final Renderer renderer;
	private final ControlDialog controlDialog;
	private final GLCanvas canvas;
	private final FPSAnimator redrawThread;
	private final SceneElement sceneElement;

	private int mouseX;
	private int mouseY;
	private int width;
	private int height;

	public static void main(String[] args) {
		new DemoWindow(new Scene(), 500, 500);
	}

	public DemoWindow(Scene scene, int width, int height) {

		// Scene.
		sceneElement = new SceneElement(scene);
		renderer = new Renderer(sceneElement);

		// Frame.
		this.width = width;
		this.height = height;
		setSize(width, height);
		addWindowListener(this);

		// Select a multisample capability with 8 samples per pixel.
		GLCapabilities caps = new GLCapabilities(null);
		caps.setSampleBuffers(true);
		caps.setNumSamples(8);

		// Canvas.
		canvas = new GLCanvas(caps, null, null, null);
		canvas.addGLEventListener(this);
		canvas.addMouseMotionListener(this);
		add(canvas);

		// Control dialog.
		controlDialog = new ControlDialog(this, scene);

		// Start redraw thread.
		redrawThread = new FPSAnimator(canvas, 60);
		redrawThread.start();

		// Display.
		setVisible(true);
		controlDialog.setVisible(true);
	}

	public void display(GLAutoDrawable drawable) {
		renderer.display(drawable.getGL().getGL2());
	}

	public void dispose(GLAutoDrawable drawable) {
	}

	public void init(GLAutoDrawable drawable) {
		renderer.initiate(drawable.getGL().getGL2());
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		this.width = width;
		this.height = height;
		renderer.reshape(drawable.getGL().getGL2(), width, height);
	}

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

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {

		// Finalize OpenGL on window close.
		remove(canvas);
		dispose();
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}
}
