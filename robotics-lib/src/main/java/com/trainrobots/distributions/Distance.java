/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Region;
import com.trainrobots.observables.Robot;
import com.trainrobots.scenes.Position;

public class Distance {

	private Distance() {
	}

	public static Double weightOfDistance(Observable observable1,
			Observable observable2) {
		Double distance = distance(observable1, observable2);
		if (distance == null) {
			return null;
		}
		double value = distance.doubleValue();
		return distance != null ? 1 / (value + 1) : null;
	}

	public static double distance(Position p1, Position p2) {
		return distance(p1.x(), p1.y(), p2.x(), p2.y());
	}

	private static Double distance(Observable observable1,
			Observable observable2) {

		// Left.
		Position p1 = observable1.referencePoint();
		if (p1 == null) {
			return null;
		}

		// Right.
		Position p2 = observable2.referencePoint();
		if (p2 != null) {
			return distance(p1.x(), p1.y(), p2.x(), p2.y());
		}

		// Edge.
		if (observable2 instanceof Edge) {
			Edge edge = (Edge) observable2;
			switch (edge.indicator()) {
			case Left:
				return new Double(7 - p1.y());
			case Right:
				return new Double(p1.y());
			case Front:
				return new Double(7 - p1.x());
			case Back:
				return new Double(p1.x());
			}
		}

		// Region.
		if (observable2 instanceof Region) {
			Region region = (Region) observable2;
			switch (region.indicator()) {
			case Left:
				return new Double(7 - p1.y());
			case Right:
				return new Double(p1.y());
			case Front:
				return new Double(7 - p1.x());
			case Back:
				return new Double(p1.x());
			case Center:
				return distance(p1.x(), p1.y(), 3.5, 3.5);
			}
		}

		// Robot.
		if (observable2 instanceof Robot) {
			return new Double(p1.x());
		}

		// Not supported.
		return null;
	}

	private static double distance(double x1, double y1, double x2, double y2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		return Math.sqrt(dx * dx + dy * dy);
	}
}