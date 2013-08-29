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

import java.awt.Color;
import java.util.ArrayList;

import javax.media.opengl.GL2;

import com.trainrobots.ui.configuration.Block;
import com.trainrobots.ui.configuration.Configuration;

public class RobotControl {

	private Robot m_robot = new Robot();
	private BoardState m_board = new BoardState();

	private int m_x = 3;
	private int m_y = 3;
	private int m_z = 4;
	private boolean m_grasp;

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

	public void toggleGrasp() {
		m_grasp = !m_grasp;
		if (m_grasp) {
			m_robot.setGrasp(0.8);

		} else {
			m_robot.setGrasp(0.6);
			m_board.release(m_x, m_y, m_z);
		}
	}

	public void addObject(Block block) {
		if (block.type == Block.CUBE) {
			m_board.addCube(block.x, block.y, block.z,
					toRobotColor(block.color), block);
		} else {
			m_board.addPyramid(block.x, block.y, block.z,
					toRobotColor(block.color), block);
		}
	}

	public void addCube(char color) {
		for (int z = 0; z < 8; z++) {
			if (m_board.get(m_x, m_y, z) == null) {
				addObject(new Block(color, Block.CUBE, m_x, m_y, z));
				return;
			}
		}
	}

	public void addPyramid(char color) {
		for (int z = 0; z < 8; z++) {
			if (m_board.get(m_x, m_y, z) == null) {
				addObject(new Block(color, Block.PYRAMID, m_x, m_y, z));
				return;
			}
		}
	}

	public void remove() {
		for (int z = 7; z >= 0; z--) {
			if (m_board.get(m_x, m_y, z) != null) {
				m_board.clear(m_x, m_y, z);
				return;
			}
		}
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

	public void loadConfiguration(Configuration configuration) {

		// Clear board.
		m_board.clear();

		// Position arm.
		m_grasp = false;
		m_robot.setGrasp(0.6);
		moveArm(configuration.armX, configuration.armY, configuration.armZ);
		m_board.setArmLocation(configuration.armX, configuration.armY);

		// Add objects.
		for (Block block : configuration.blocks) {
			addObject(block);
		}

		// Close gripper?
		if (!configuration.gripperOpen) {
			toggleGrasp();
		}
	}

	public Configuration saveConfiguration() {

		Configuration configuration = new Configuration();
		configuration.armX = m_x;
		configuration.armY = m_y;
		configuration.armZ = m_z;
		configuration.gripperOpen = !m_grasp;

		configuration.blocks = new ArrayList<Block>();

		for (int i = 0, j, k; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				for (k = 0; k < 8; k++) {
					PolyMesh mesh = m_board.get(i, j, k);
					if (mesh != null && mesh.tag != null) {
						Block b = (Block) mesh.tag;
						configuration.blocks.add(new Block(b.color, b.type, i,
								j, k));
					}
				}
			}
		}

		return configuration;
	}

	private Color toRobotColor(char color) {
		switch (color) {
		case Block.BLUE:
			return Color.BLUE;
		case Block.CYAN:
			return Color.CYAN;
		case Block.RED:
			return Color.RED;
		case Block.YELLOW:
			return Color.YELLOW;
		case Block.GREEN:
			return Color.GREEN;
		case Block.MAGENTA:
			return Color.MAGENTA;
		case Block.GRAY:
			return Color.GRAY;
		}
		return Color.WHITE;
	}
}
