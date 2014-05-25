/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.math;

public class Matrices {

	private Matrices() {
	}

	public static Matrix translate(Vector vector) {
		return translate(vector.x(), vector.y(), vector.z());
	}

	public static Matrix translate(double x, double y, double z) {
		return new Matrix(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, x, y, z, 1);
	}

	public static Matrix rotateX(double a) {
		a = radians(a);
		double sin = Math.sin(a);
		double cos = Math.cos(a);
		return new Matrix(1, 0, 0, 0, 0, cos, -sin, 0, 0, sin, cos, 0, 0, 0, 0,
				1);
	}

	public static Matrix rotateY(double a) {
		a = radians(a);
		double sin = Math.sin(a);
		double cos = Math.cos(a);
		return new Matrix(cos, 0, sin, 0, 0, 1, 0, 0, -sin, 0, cos, 0, 0, 0, 0,
				1);
	}

	public static Matrix rotateZ(double a) {
		a = radians(a);
		double sin = Math.sin(a);
		double cos = Math.cos(a);
		return new Matrix(cos, -sin, 0, 0, sin, cos, 0, 0, 0, 0, 1, 0, 0, 0, 0,
				1);
	}

	private static double radians(double degrees) {
		return degrees * 0.01745329;
	}
}