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