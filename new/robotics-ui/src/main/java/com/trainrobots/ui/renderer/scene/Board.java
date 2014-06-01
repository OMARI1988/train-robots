/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

import java.awt.Color;

import javax.media.opengl.GL2;

import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;
import com.trainrobots.scenes.Type;
import com.trainrobots.ui.renderer.math.Vector;

public class Board implements Element {

	private final Model board = ModelLoader.load("board");
	private final Model[][][] shapes = new Model[8][8][8];
	private Vector translation = new Vector(0, 0, 0);
	private int shadowX = 0;
	private int shadowY = 0;

	private static double boardSize = 43;
	private static double shapeSize = 4;
	private static double cellSize = boardSize / 8;
	private static double cellOffset = 0.5 * (cellSize - shapeSize);

	public Board() {
		clear();
	}

	public void translation(double x, double y, double z) {
		translation = new Vector(x, y, z);
	}

	public void shadow(Position position) {
		shadowX = position.x();
		shadowY = position.y();
	}

	public Vector getCellCenter(int x, int y, int z) {
		return new Vector(x * cellSize + 0.5f + translation.x() + 0.5
				* shapeSize, (float) (z * shapeSize) + translation.y(), -y
				* cellSize - 0.5f + translation.z() - 0.5 * shapeSize);
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
		shapes[x][y][z] = shape.type() == Type.Cube ? new Cube(shapeSize,
				shapeSize, shapeSize, ambient, diffuse) : new Prism(shapeSize,
				shapeSize, shapeSize, ambient, diffuse);
	}

	public void render(GL2 gl) {
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glPushMatrix();

		gl.glTranslatef((float) translation.x(), (float) translation.y(),
				(float) translation.z());

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, -2.0f, 0.0f);
		board.render(gl);
		gl.glPopMatrix();

		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

		gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_DEPTH_TEST);

		gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_LINE_SMOOTH);

		gl.glColor3d(0.2, 0.2, 0.2);
		gl.glBegin(GL2.GL_LINES);
		for (double x = 0.0; x <= boardSize; x += cellSize) {
			gl.glVertex3d(x, 0.0, 0.0);
			gl.glVertex3d(x, 0.0, -boardSize);
			gl.glVertex3d(0.0, 0.0, -x);
			gl.glVertex3d(boardSize, 0.0, -x);
		}
		gl.glEnd();

		gl.glLineWidth(2.0f);

		double x0 = shadowX * cellSize, y0 = -shadowY * cellSize, x1 = (shadowX + 1)
				* cellSize, y1 = -(shadowY + 1) * cellSize;

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

		// Render shapes.
		for (int i = 0, j, k; i < shapes.length; ++i) {
			for (j = 0; j < shapes[i].length; ++j) {
				for (k = 0; k < shapes[i][j].length; ++k) {
					if (shapes[i][j][k] != null) {
						gl.glPushMatrix();
						gl.glTranslatef((float) (i * cellSize + cellOffset),
								(float) (k * shapeSize),
								(float) (-j * cellSize - cellOffset));
						shapes[i][j][k].render(gl);
						gl.glPopMatrix();
					}
				}
			}
		}

		gl.glPopMatrix();
		gl.glPopAttrib();
	}

	public void clear() {
		for (int i = 0, j, k; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				for (k = 0; k < 8; k++) {
					shapes[i][j][k] = null;
				}
			}
		}
	}
}