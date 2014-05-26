/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

import javax.media.opengl.GL2;

import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Scene;
import com.trainrobots.scenes.SceneListener;
import com.trainrobots.scenes.Shape;

public class SceneElement implements Element {

	private final Board board = new Board();
	private final Robot robot = new Robot();
	private float m_rotx = 37.0f; // rotation about the x axis
	private float m_roty = -42.5f; // rotation about the y axis
	private Scene scene;

	public SceneElement() {
		this(new Scene());
	}

	public SceneElement(Scene scene) {
		board.translation(0.0, 0.0, 22.5);
		robot.translation(-20.0, 0.0, 0.0);
		scene(scene);
	}

	public void scene(Scene scene) {

		// Subscribe to events.
		this.scene = scene;
		scene.listener(new SceneListener() {

			public void gripperPositionChanged(Position position) {
				bindToGripperPosition();
			}

			public void gripperOpenChanged(boolean open) {
				bindToGripperOpen();
			}
		});

		// Bind gripper.
		bindToGripperPosition();
		bindToGripperOpen();

		// Bind shapes.
		board.clear();
		for (Shape shape : scene.shapes()) {
			board.add(shape);
		}
	}

	public void incrementRotation(float dx, float dy) {
		m_rotx += dx;
		m_roty += dy;
	}

	public void render(GL2 gl) {

		// Rotate.
		gl.glRotatef(m_rotx, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(m_roty, 0.0f, 1.0f, 0.0f);

		// Render.
		board.render(gl);
		robot.render(gl);
	}

	private void bindToGripperPosition() {
		// Iterative update of robot to avoid awkward angles.
		Position p = scene.gripper().position();
		robot.resetAngles();
		robot.computeAngles(board.getCellCenter(p.x(), 0, 5));
		robot.computeAngles(board.getCellCenter(p.x(), p.y(), 5));
		robot.computeAngles(board.getCellCenter(p.x(), p.y(), p.z()));
		board.shadow(p);
	}

	private void bindToGripperOpen() {
		robot.setGrasp(scene.gripper().open() ? 0.6 : 0.8);
	}
}