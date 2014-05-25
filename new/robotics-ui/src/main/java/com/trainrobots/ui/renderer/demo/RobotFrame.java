/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.demo;

import java.awt.event.*;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;

import com.jogamp.opengl.util.FPSAnimator;
import com.trainrobots.ui.renderer.Controller;
import com.trainrobots.ui.renderer.Renderer;

public class RobotFrame extends Renderer implements GLEventListener,
		MouseMotionListener, WindowListener {
	private int m_x, // x coordinate of the mouse
			m_y; // y coordinate of the mouse

	private RobotControlDialog m_control; // user interface for moving the robot
											// arm

	private JFrame m_frame;

	private GLCanvas m_canvas; // opengl canvas
	private FPSAnimator m_redraw; // thread to redraw window

	public static void main(String[] args) {
		Controller rc = new Controller();
		new RobotFrame(rc, 500, 500);
	}

	public RobotFrame(Controller rbt, int w, int h) {
		super(rbt, w, h);

		m_frame = new JFrame();
		m_frame.setSize(m_width, m_height);
		m_frame.addWindowListener(this);

		// choose a multisample capability with 8 samples per pixel
		GLCapabilities caps = new GLCapabilities(null);
		caps.setSampleBuffers(true);
		caps.setNumSamples(8);

		// construct the canvas
		m_canvas = new GLCanvas(caps, null, null, null);
		m_canvas.addGLEventListener(this);
		m_canvas.addMouseMotionListener(this);

		m_frame.add(m_canvas);

		// construct the control dialog
		m_control = new RobotControlDialog(m_frame, m_rbtctrl);

		// start the redraw thread
		m_redraw = new FPSAnimator(m_canvas, 60);
		m_redraw.start();

		m_frame.setVisible(true);
		m_control.setVisible(true);
	}

	// method to redraw the opengl scene
	public void display(GLAutoDrawable drawable) {
		display(drawable.getGL().getGL2());
	}

	public void dispose(GLAutoDrawable drawable) {
	}

	// init gl state
	public void init(GLAutoDrawable drawable) {
		init(drawable.getGL().getGL2());
	}

	// reshape function maintains correct orthographic projection
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		m_width = width;
		m_height = height;
		reshape(drawable.getGL().getGL2(), m_width, m_height);
	}

	// on mouse drag rotate the view
	public void mouseDragged(MouseEvent e) {
		int dx = e.getX() - m_x, dy = e.getY() - m_y;
		float mx = ((float) dx) / m_width, my = ((float) dy) / m_height;

		m_rotx += my * 180.0f;
		m_roty += mx * 180.0f;

		m_x = e.getX();
		m_y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		m_x = e.getX();
		m_y = e.getY();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	// finalize opengl on window close
	public void windowClosing(WindowEvent e) {
		m_frame.remove(m_canvas);
		m_frame.dispose();
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
