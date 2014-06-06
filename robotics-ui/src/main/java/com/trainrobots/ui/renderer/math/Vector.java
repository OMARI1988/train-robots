/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.math;

public class Vector {

	private final double x;
	private final double y;
	private final double z;

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	public Vector subtract(Vector v) {
		return new Vector(x - v.x, y - v.y, z - v.z);
	}

	public double dot(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public Vector cross(Vector v) {
		return new Vector(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x
				* y);
	}

	public Vector normalize() {
		double length = Math.sqrt(x * x + y * y + z * z);
		return new Vector(x / length, y / length, z / length);
	}

	public double distanceSquared(Vector v) {
		double dx = v.x - x;
		double dy = v.y - y;
		double dz = v.z - z;
		return dx * dx + dy * dy + dz * dz;
	}
}