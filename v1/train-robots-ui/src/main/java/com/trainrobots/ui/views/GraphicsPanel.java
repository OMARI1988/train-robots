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

package com.trainrobots.ui.views;

import java.awt.BorderLayout;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JPanel;

import com.jogamp.opengl.util.FPSAnimator;
import com.trainrobots.ui.robot.RobotControl;
import com.trainrobots.ui.robot.RobotRenderer;

public class GraphicsPanel extends JPanel {

	private final RobotControl control = new RobotControl();

	public GraphicsPanel(int width, int height) {

		GLCapabilities caps = new GLCapabilities(null);
		caps.setSampleBuffers(true);
		caps.setNumSamples(8);

		final RobotRenderer renderer = new RobotRenderer(control, width, height);

		GLJPanel panel = new GLJPanel(caps);
		panel.addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable drawable, int x, int y,
					int width, int height) {
				renderer.reshape(drawable.getGL().getGL2(), width, height);
			}

			@Override
			public void init(GLAutoDrawable drawable) {
				renderer.init(drawable.getGL().getGL2());
			}

			@Override
			public void dispose(GLAutoDrawable drawable) {
			}

			@Override
			public void display(GLAutoDrawable drawable) {
				renderer.display(drawable.getGL().getGL2());
			}
		});

		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);

		FPSAnimator redraw = new FPSAnimator(panel, 60);
		redraw.start();
	}

	public RobotControl getRobotControl() {
		return control;
	}
}