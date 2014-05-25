/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer;

import javax.media.opengl.GL2;

import com.trainrobots.scenes.Scene;
import com.trainrobots.scenes.Shape;
import com.trainrobots.scenes.Type;
import com.trainrobots.ui.renderer.models.Model;

public class Controller {

	private Robot m_robot = new Robot();
	private Board m_board = new Board();

	private int m_x = 3;
	private int m_y = 3;
	private int m_z = 4;
	private boolean m_grasp;

	public Controller() {
		m_board.setTranslate(0.0, 0.0, 22.5);
		m_board.setShadow(m_x, m_y);

		m_robot.setTranslate(-20.0, 0.0, 0.0);
		m_robot.computeAngles(m_board.getCoord(m_x, m_y, m_z));
		m_robot.setGrasp(0.6);
	}

	public void moveArm(int x, int y, int z) {
		m_robot.resetAngles();

		// iterative update of robot to avoid awkward angles
		m_robot.computeAngles(m_board.getCoord(x, 0, 5));
		m_robot.computeAngles(m_board.getCoord(x, y, 5));
		m_robot.computeAngles(m_board.getCoord(x, y, z));
		if (m_grasp) {
			m_board.moveObj(m_x, m_y, m_z, x, y, z);
		}
		m_x = x;
		m_y = y;
		m_z = z;
		m_board.setShadow(m_x, m_y);
	}

	public void toggleGrasp() {
		m_grasp = !m_grasp;
		if (m_grasp) {
			m_robot.setGrasp(0.8);

		} else {
			m_robot.setGrasp(0.6);
			m_board.release(m_x, m_y, m_z);
		}
	}

	public void addObject(Shape shape) {
		if (shape.type() == Type.Cube) {
			m_board.addCube(shape.position().x(), shape.position().y(), shape
					.position().z(), shape.color().color());
		} else {
			m_board.addPrism(shape.position().x(), shape.position().y(), shape
					.position().z(), shape.color().color());
		}
	}

	public void render(GL2 gl) {
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		m_board.render(gl);
		m_robot.render(gl, Model.FACE_NORMALS);
		gl.glPopAttrib();
	}

	public void loadScene(Scene scene) {

		// Clear board.
		m_board.clear();

		// Position arm.
		m_grasp = false;
		m_robot.setGrasp(0.6);
		if (scene == null) {
			moveArm(3, 3, 4);
			m_board.setShadow(3, 3);
			return;
		}
		moveArm(scene.gripper().position().x(), scene.gripper().position().y(),
				scene.gripper().position().z());
		m_board.setShadow(scene.gripper().position().x(), scene.gripper()
				.position().y());

		// Add objects.
		for (Shape shape : scene.shapes()) {
			addObject(shape);
		}

		// Close gripper?
		if (!scene.gripper().open()) {
			toggleGrasp();
		}
	}
}
