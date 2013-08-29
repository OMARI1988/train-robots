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

package com.trainrobots.ui.robot;

import javax.media.opengl.*;

public class RobotControl {

	// object types
	public static final int CUBE = 0;
	public static final int PYRAMID = 1;

	// object colors
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	public static final int YELLOW = 3;
	public static final int CYAN = 4;
	public static final int MAGENTA = 5;

	Robot m_robot = new Robot(); // robot arm
	BoardState m_board = new BoardState(); // board state

	int m_x = 3, m_y = 3, m_z = 4; // initial arm position
	boolean m_grasp = false; // initially not grasping

	public RobotControl() {
		m_board.setTranslate(0.0, 0.0, 22.5);
		m_board.setArmLocation(m_x, m_y);

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
		m_board.setArmLocation(m_x, m_y);
	}

	public void grasp() {
		m_grasp = !m_grasp;
		if (m_grasp) {
			m_robot.setGrasp(0.8);

		} else {
			m_robot.setGrasp(0.6);
			m_board.release(m_x, m_y, m_z);
		}
	}

	public void addObject(int x, int y, int z, int c, int type) {
		float[][] colors = { { 1.0f, 0.0f, 0.0f }, { 0.0f, 1.0f, 0.0f },
				{ 0.0f, 0.0f, 1.0f }, { 1.0f, 1.0f, 0.0f },
				{ 0.0f, 1.0f, 1.0f }, { 1.0f, 0.0f, 1.0f } };

		if (type == CUBE)
			m_board.addCube(x, y, z, colors[c][0], colors[c][1], colors[c][2]);
		else
			m_board.addPyramid(x, y, z, colors[c][0], colors[c][1],
					colors[c][2]);
	}

	public void render(GL2 gl) {
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		m_board.render(gl);
		m_robot.render(gl, PolyMesh.FACE_NORMALS, true);
		gl.glPopAttrib();
	}

	public void moveUp() {
		if (m_x > 0) {
			moveArm(m_x - 1, m_y, m_z);
		}
	}

	public void moveDown() {
		if (m_x < 7) {
			moveArm(m_x + 1, m_y, m_z);
		}
	}

	public void moveLeft() {
		if (m_y > 0) {
			moveArm(m_x, m_y - 1, m_z);
		}
	}

	public void moveRight() {
		if (m_y < 7) {
			moveArm(m_x, m_y + 1, m_z);
		}
	}

	public void lowerArm() {
		if (m_z > 0) {
			moveArm(m_x, m_y, m_z - 1);
		}
	}

	public void raiseArm() {
		if (m_z < 7) {
			moveArm(m_x, m_y, m_z + 1);
		}
	}
}
