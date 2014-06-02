/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.math;

public class Matrix {

	private final double[][] e;

	public Matrix(double e00, double e01, double e02, double e03, double e10,
			double e11, double e12, double e13, double e20, double e21,
			double e22, double e23, double e30, double e31, double e32,
			double e33) {
		e = new double[][] { { e00, e01, e02, e03 }, { e10, e11, e12, e13 },
				{ e20, e21, e22, e23 }, { e30, e31, e32, e33 } };
	}

	public Matrix translate(double x, double y, double z) {
		return multiply(Matrices.translate(x, y, z));
	}

	public Matrix rotateX(double a) {
		return multiply(Matrices.rotateX(a));
	}

	public Matrix rotateY(double a) {
		return multiply(Matrices.rotateY(a));
	}

	public Matrix rotateZ(double a) {
		return multiply(Matrices.rotateZ(a));
	}

	public Vector multiply(Vector v) {
		double x = v.x() * e[0][0] + v.y() * e[1][0] + v.z() * e[2][0]
				+ e[3][0];
		double y = v.x() * e[0][1] + v.y() * e[1][1] + v.z() * e[2][1]
				+ e[3][1];
		double z = v.x() * e[0][2] + v.y() * e[1][2] + v.z() * e[2][2]
				+ e[3][2];
		return new Vector(x, y, z);
	}

	private Matrix multiply(Matrix m) {
		double e00 = m.e[0][0] * e[0][0] + m.e[0][1] * e[1][0] + m.e[0][2]
				* e[2][0] + m.e[0][3] * e[3][0];
		double e01 = m.e[0][0] * e[0][1] + m.e[0][1] * e[1][1] + m.e[0][2]
				* e[2][1] + m.e[0][3] * e[3][1];
		double e02 = m.e[0][0] * e[0][2] + m.e[0][1] * e[1][2] + m.e[0][2]
				* e[2][2] + m.e[0][3] * e[3][2];
		double e03 = m.e[0][0] * e[0][3] + m.e[0][1] * e[1][3] + m.e[0][2]
				* e[2][3] + m.e[0][3] * e[3][3];
		double e10 = m.e[1][0] * e[0][0] + m.e[1][1] * e[1][0] + m.e[1][2]
				* e[2][0] + m.e[1][3] * e[3][0];
		double e11 = m.e[1][0] * e[0][1] + m.e[1][1] * e[1][1] + m.e[1][2]
				* e[2][1] + m.e[1][3] * e[3][1];
		double e12 = m.e[1][0] * e[0][2] + m.e[1][1] * e[1][2] + m.e[1][2]
				* e[2][2] + m.e[1][3] * e[3][2];
		double e13 = m.e[1][0] * e[0][3] + m.e[1][1] * e[1][3] + m.e[1][2]
				* e[2][3] + m.e[1][3] * e[3][3];
		double e20 = m.e[2][0] * e[0][0] + m.e[2][1] * e[1][0] + m.e[2][2]
				* e[2][0] + m.e[2][3] * e[3][0];
		double e21 = m.e[2][0] * e[0][1] + m.e[2][1] * e[1][1] + m.e[2][2]
				* e[2][1] + m.e[2][3] * e[3][1];
		double e22 = m.e[2][0] * e[0][2] + m.e[2][1] * e[1][2] + m.e[2][2]
				* e[2][2] + m.e[2][3] * e[3][2];
		double e23 = m.e[2][0] * e[0][3] + m.e[2][1] * e[1][3] + m.e[2][2]
				* e[2][3] + m.e[2][3] * e[3][3];
		double e30 = m.e[3][0] * e[0][0] + m.e[3][1] * e[1][0] + m.e[3][2]
				* e[2][0] + m.e[3][3] * e[3][0];
		double e31 = m.e[3][0] * e[0][1] + m.e[3][1] * e[1][1] + m.e[3][2]
				* e[2][1] + m.e[3][3] * e[3][1];
		double e32 = m.e[3][0] * e[0][2] + m.e[3][1] * e[1][2] + m.e[3][2]
				* e[2][2] + m.e[3][3] * e[3][2];
		double e33 = m.e[3][0] * e[0][3] + m.e[3][1] * e[1][3] + m.e[3][2]
				* e[2][3] + m.e[3][3] * e[3][3];
		return new Matrix(e00, e01, e02, e03, e10, e11, e12, e13, e20, e21,
				e22, e23, e30, e31, e32, e33);
	}
}
