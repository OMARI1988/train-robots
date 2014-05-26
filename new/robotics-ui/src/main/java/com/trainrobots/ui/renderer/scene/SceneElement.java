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
import com.trainrobots.scenes.Shape;

public class SceneElement implements Element {

	private final Board board = new Board();
	private final Robot robot = new Robot();
	private Position arm = new Position(3, 3, 4);
	private boolean gripperOpen;
	private float m_rotx = 37.0f; // rotation about the x axis
	private float m_roty = -42.5f; // rotation about the y axis

	public SceneElement() {

		// Board.
		board.setTranslate(0.0, 0.0, 22.5);
		board.setShadow(arm);

		// Robot.
		robot.setTranslate(-20.0, 0.0, 0.0);
		robot.computeAngles(board.getCoord(arm.x(), arm.y(), arm.z()));
		robot.setGrasp(0.6);
	}

	public SceneElement(Scene scene) {
		this();
		scene(scene);
	}

	public void scene(Scene scene) {

		// Clear board.
		board.clear();

		// Position arm.
		gripperOpen = true;
		robot.setGrasp(0.6);
		moveArm(scene.gripper().position());
		board.setShadow(scene.gripper().position());

		// Add objects.
		for (Shape shape : scene.shapes()) {
			board.add(shape);
		}

		// Close gripper?
		if (!scene.gripper().open()) {
			toggleGripper();
		}
	}

	public void moveArm(Position p) {

		// Iterative update of robot to avoid awkward angles.
		robot.resetAngles();
		robot.computeAngles(board.getCoord(p.x(), 0, 5));
		robot.computeAngles(board.getCoord(p.x(), p.y(), 5));
		robot.computeAngles(board.getCoord(p.x(), p.y(), p.z()));
		if (!gripperOpen) {
			board.moveObj(arm.x(), arm.y(), arm.z(), p.x(), p.y(), p.z());
		}
		arm = p;
		board.setShadow(arm);
	}

	public void toggleGripper() {
		gripperOpen = !gripperOpen;
		if (gripperOpen) {
			robot.setGrasp(0.8);
		} else {
			robot.setGrasp(0.6);
			board.release(arm.x(), arm.y(), arm.z());
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
}