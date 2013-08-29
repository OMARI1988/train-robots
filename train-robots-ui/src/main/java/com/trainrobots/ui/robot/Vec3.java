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

public class Vec3 {

	public double x = 0.0f, y = 0.0f, z = 0.0f;

	public static Vec3 cross(Vec3 v0, Vec3 v1) {
		return new Vec3(v0.y * v1.z - v1.y * v0.z, v0.z * v1.x - v1.z * v0.x,
				v0.x * v1.y - v1.x * v0.y);
	}

	public static double dot(Vec3 v0, Vec3 v1) {
		return (v0.x * v1.x + v0.y * v1.y + v0.z * v1.z);
	}

	public static Vec3 mul(Vec3 v, Mat44 m) {
		return new Vec3(v.x * m.get(0, 0) + v.y * m.get(1, 0) + v.z
				* m.get(2, 0) + m.get(3, 0), v.x * m.get(0, 1) + v.y
				* m.get(1, 1) + v.z * m.get(2, 1) + m.get(3, 1), v.x
				* m.get(0, 2) + v.y * m.get(1, 2) + v.z * m.get(2, 2)
				+ m.get(3, 2));
	}

	public Vec3() {
	}

	public Vec3(double xv, double yv, double zv) {
		x = xv;
		y = yv;
		z = zv;
	}

	public Vec3(Vec3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public boolean equals(Vec3 v) {
		return !((x != v.x) || (y != v.y) || (z != v.z));
	}

	public String toString() {
		return ("(" + x + " " + y + " " + z + ")");
	}

	public Vec3 addEq(Vec3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	public Vec3 add(Vec3 v) {
		return new Vec3(x + v.x, y + v.y, z + v.z);
	}

	public Vec3 subEq(Vec3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public Vec3 sub(Vec3 v) {
		return new Vec3(x - v.x, y - v.y, z - v.z);
	}

	public Vec3 mulEq(double s) {
		x *= s;
		y *= s;
		z *= s;
		return this;
	}

	public Vec3 mul(double s) {
		return new Vec3(x * s, y * s, z * s);
	}

	public Vec3 mulEq(Mat44 m) {
		Vec3 tmp = this.mul(m);
		x = tmp.x;
		y = tmp.y;
		z = tmp.z;
		return this;
	}

	public Vec3 mul(Mat44 m) {
		return Vec3.mul(this, m);
	}

	public Vec3 divEq(double s) {
		x /= s;
		y /= s;
		z /= s;
		return this;
	}

	public Vec3 div(double s) {
		return new Vec3(x / s, y / s, z / s);
	}

	public Vec3 cross(Vec3 v) {
		return Vec3.cross(this, v);
	}

	public double dot(Vec3 v) {
		return Vec3.dot(this, v);
	}

	public double sqlength() {
		return (x * x + y * y + z * z);
	}

	public double length() {
		return (double) Math.sqrt(sqlength());
	}

	public double sqdistance(Vec3 v) {
		double dx = v.x - x, dy = v.y - y, dz = v.z - z;
		return (dx * dx + dy * dy + dz * dz);
	}

	public double distance(Vec3 v) {
		return (double) Math.sqrt(sqdistance(v));
	}

	public void clear() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public void normalize() {
		divEq(length());
	}

	public void zeroTiny() {
		if (Math.abs(x) < 0.000001f)
			x = 0.0f;
		if (Math.abs(y) < 0.000001f)
			y = 0.0f;
		if (Math.abs(z) < 0.000001f)
			z = 0.0f;
	}

}
