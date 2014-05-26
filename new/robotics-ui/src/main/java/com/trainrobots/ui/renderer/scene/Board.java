/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

import java.awt.Color;

import javax.media.opengl.GL2;

import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;
import com.trainrobots.scenes.Type;
import com.trainrobots.ui.renderer.math.Vector;

public class Board implements Element {

	public static double BOARD_DIM = 43.0; // width and depth of the board
	public static double OBJ_DIM = 4.0; // size of an individual object
	public static double GRID_OFF = BOARD_DIM / 8.0; // size of a grid cell
	public static double CELL_OFF = 0.5 * (GRID_OFF - OBJ_DIM);

	// translation of the board from the origin
	private Vector m_translate = new Vector(0, 0, 0);

	// array storing the position of objects in the game
	private Model[][][] m_state = new Model[8][8][8];

	// board model
	private Model m_board = ModelLoader.load("board");

	// location of arm
	private int m_x = 0, m_y = 0;

	public Board() {
		clear();
	}

	public void setTranslate(double x, double y, double z) {
		m_translate = new Vector(x, y, z);
	}

	public void setShadow(Position position) {
		m_x = position.x();
		m_y = position.y();
	}

	// gets the coordinate of the centre of a grid cell, used calculating robot
	// arm angles
	public Vector getCoord(int x, int y, int z) {
		return new Vector(
				x * GRID_OFF + 0.5f + m_translate.x() + 0.5 * OBJ_DIM,
				(float) (z * OBJ_DIM) + m_translate.y(), -y * GRID_OFF - 0.5f
						+ m_translate.z() - 0.5 * OBJ_DIM);
	}

	public void add(Shape shape) {
		int x = shape.position().x();
		int y = shape.position().y();
		int z = shape.position().z();
		Color color = shape.color().color();
		float r = color.getRed() / 255f;
		float g = color.getGreen() / 255f;
		float b = color.getBlue() / 255f;
		float[] ambient = { 0.0f, 0.0f, 0.0f, 1.0f };
		float[] diffuse = { r, g, b, 1.0f };
		m_state[x][y][z] = shape.type() == Type.Cube ? new Cube(OBJ_DIM,
				OBJ_DIM, OBJ_DIM, ambient, diffuse) : new Prism(OBJ_DIM,
				OBJ_DIM, OBJ_DIM, ambient, diffuse);
	}

	public void moveObj(int sx, int sy, int sz, int dx, int dy, int dz) {
		// moves and object from (sx, sy, sz) -> (dx, dy, dz)
		if (m_state[sx][sy][sz] != null && m_state[dx][dy][dz] == null) {
			m_state[dx][dy][dz] = m_state[sx][sy][sz];
			m_state[sx][sy][sz] = null;
		}
	}

	public void release(int x, int y, int z) {
		// release the object at (x, y, z), if z > 0 drops it to the first
		// unoccupied grid location.
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

	public void render(GL2 gl) {
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glPushMatrix();

		gl.glTranslatef((float) m_translate.x(), (float) m_translate.y(),
				(float) m_translate.z());

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, -2.0f, 0.0f);
		m_board.render(gl);
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
						m_state[i][j][k].render(gl);
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

	public Model get(int i, int j, int k) {
		return m_state[i][j][k];
	}

	public void clear(int i, int j, int k) {
		m_state[i][j][k] = null;
	}
}