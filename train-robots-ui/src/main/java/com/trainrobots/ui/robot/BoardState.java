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

import javax.media.opengl.*;

public class BoardState {

	public static double BOARD_DIM = 43.0; // width and depth of the board
	public static double OBJ_DIM = 4.0; // size of an individual object
	public static double GRID_OFF = BOARD_DIM / 8.0; // size of a grid cell
	public static double CELL_OFF = 0.5 * (GRID_OFF - OBJ_DIM);

	private Vec3 m_translate = new Vec3(); // translation of the board from the
											// origin
	private PolyMesh[][][] m_state = new PolyMesh[8][8][8]; // array storing the
															// position of
															// objects in the
															// game
	private PolyMesh m_board = (new OBJReader()).readMesh("Board.obj"); // board
																		// mesh
	private int m_x = 0, m_y = 0; // location of arm

	public BoardState() {
		clear();
	}

	public void setTranslate(double x, double y, double z) {
		m_translate.x = x;
		m_translate.y = y;
		m_translate.z = z;
	}

	public void setArmLocation(int x, int y) {
		m_x = x;
		m_y = y;
	}

	// gets the coordinate of the centre of a grid cell, used calculating robot
	// arm angles
	public Vec3 getCoord(int x, int y, int z) {
		return new Vec3(x * GRID_OFF + 0.5f + m_translate.x + 0.5 * OBJ_DIM,
				(float) (z * OBJ_DIM) + m_translate.y, -y * GRID_OFF - 0.5f
						+ m_translate.z - 0.5 * OBJ_DIM);
	}

	// adds a pyramid at the appropriate grid cell
	public void addPyramid(int x, int y, int z, Color color, Object tag) {
		if (m_state[x][y][z] == null) {
			m_state[x][y][z] = PolyMesh.pyramid(OBJ_DIM, OBJ_DIM, OBJ_DIM);
			m_state[x][y][z].tag = tag;
			Material m = m_state[x][y][z].getMaterial();

			float r = color.getRed() / 255f;
			float g = color.getGreen() / 255f;
			float b = color.getBlue() / 255f;

			m.setDiffuse(r, g, b, 1.0f);
			m.setAmbient(0.0f, 0.0f, 0.0f, 1.0f);
		}
	}

	// adds a cube at the appropriate grid cell
	public void addCube(int x, int y, int z, Color color, Object tag) {
		if (m_state[x][y][z] == null) {
			m_state[x][y][z] = PolyMesh.cube(OBJ_DIM, OBJ_DIM, OBJ_DIM);
			m_state[x][y][z].tag = tag;
			Material m = m_state[x][y][z].getMaterial();

			float r = color.getRed() / 255f;
			float g = color.getGreen() / 255f;
			float b = color.getBlue() / 255f;

			m.setDiffuse(r, g, b, 1.0f);
			m.setAmbient(0.0f, 0.0f, 0.0f, 1.0f);
		}
	}

	// moves and object from (sx, sy, sz) -> (dx, dy, dz)
	public void moveObj(int sx, int sy, int sz, int dx, int dy, int dz) {
		if (m_state[sx][sy][sz] != null && m_state[dx][dy][dz] == null) {
			m_state[dx][dy][dz] = m_state[sx][sy][sz];
			m_state[sx][sy][sz] = null;
		}
	}

	// release the object at (x, y, z), if z > 0 drops it to the first
	// unoccupied grid location
	public void release(int x, int y, int z) {
		if (m_state[x][y][z] != null) {
			for (int i = 0; i < z; ++i) {
				if (m_state[x][y][i] == null) {
					m_state[x][y][i] = m_state[x][y][z];
					m_state[x][y][z] = null;
					break;
				}
			}
		}
	}

	// render the board state
	public void render(GL2 gl) {
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glPushMatrix();

		gl.glTranslatef((float) m_translate.x, (float) m_translate.y,
				(float) m_translate.z);

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, -2.0f, 0.0f);
		m_board.render(gl, PolyMesh.FACE_NORMALS, true); // render the board
		gl.glPopMatrix();

		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

		gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_DEPTH_TEST);

		gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_LINE_SMOOTH);

		gl.glColor3d(0.2, 0.2, 0.2);
		gl.glBegin(GL2.GL_LINES);
		for (double x = 0.0; x <= BOARD_DIM; x += GRID_OFF) {
			gl.glVertex3d(x, 0.0, 0.0);
			gl.glVertex3d(x, 0.0, -BOARD_DIM);
			gl.glVertex3d(0.0, 0.0, -x);
			gl.glVertex3d(BOARD_DIM, 0.0, -x);
		}
		gl.glEnd();

		gl.glLineWidth(2.0f);

		double x0 = m_x * GRID_OFF, y0 = -m_y * GRID_OFF, x1 = (m_x + 1)
				* GRID_OFF, y1 = -(m_y + 1) * GRID_OFF;

		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3d(x0, 0.0, y0);
		gl.glVertex3d(x0, 0.0, y1);
		gl.glVertex3d(x0, 0.0, y1);
		gl.glVertex3d(x1, 0.0, y1);
		gl.glVertex3d(x1, 0.0, y1);
		gl.glVertex3d(x1, 0.0, y0);
		gl.glVertex3d(x1, 0.0, y0);
		gl.glVertex3d(x0, 0.0, y0);
		gl.glEnd();

		gl.glPopAttrib();

		// render all the objects in the game
		for (int i = 0, j, k; i < m_state.length; ++i)
			for (j = 0; j < m_state[i].length; ++j)
				for (k = 0; k < m_state[i][j].length; ++k) {
					if (m_state[i][j][k] != null) {
						gl.glPushMatrix();
						gl.glTranslatef((float) (i * GRID_OFF + CELL_OFF),
								(float) (k * OBJ_DIM),
								(float) (-j * GRID_OFF - CELL_OFF));
						m_state[i][j][k]
								.render(gl, PolyMesh.FACE_NORMALS, true);
						gl.glPopMatrix();
					}
				}

		gl.glPopMatrix();
		gl.glPopAttrib();
	}

	public void clear() {
		// initialize the state so that there are no objects
		for (int i = 0, j, k; i < m_state.length; ++i) {
			for (j = 0; j < m_state[i].length; ++j) {
				for (k = 0; k < m_state[i][j].length; ++k) {
					m_state[i][j][k] = null;
				}
			}
		}
	}

	public PolyMesh get(int i, int j, int k) {
		return m_state[i][j][k];
	}

	public void clear(int i, int j, int k) {
		m_state[i][j][k] = null;
	}
}