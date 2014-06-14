/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Types;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class ShapePair extends Observable {

	private final Shape shape1;
	private final Shape shape2;

	public ShapePair(Shape shape1, Shape shape2) {
		this.shape1 = shape1;
		this.shape2 = shape2;
	}

	public Shape shape1() {
		return shape1;
	}

	public Shape shape2() {
		return shape2;
	}

	public Position midPoint() {
		Position p1 = shape1.position();
		Position p2 = shape2.position();
		if (p1.z() != p2.z()) {
			throw new RoboticException("Shape pair not aligned.");
		}
		return new Position(midPoint(p1.x(), p2.x()), midPoint(p1.y(), p2.y()),
				p1.z());
	}

	@Override
	public Losr toLosr() {
		return new Entity(Types.CubeGroup);
	}

	private int midPoint(int a, int b) {
		int sum = a + b;
		if (sum % 2 != 0) {
			throw new RoboticException("Shape pair not aligned.");
		}
		return sum / 2;
	}
}