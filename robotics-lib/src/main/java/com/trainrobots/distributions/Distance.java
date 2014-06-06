/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Region;
import com.trainrobots.observables.Robot;
import com.trainrobots.observables.Stack;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class Distance {

	private Distance() {
	}

	public static Double weight(Observable observable, Observable landmark) {
		Double distance = distance(observable, landmark);
		if (distance == null) {
			return null;
		}
		double value = distance.doubleValue();
		return distance != null ? 1 / (value + 1) : null;
	}

	private static Double distance(Observable observable, Observable landmark) {

		// Left-hand-side.
		Position p1 = null;
		if (observable instanceof Shape) {
			p1 = ((Shape) observable).position();
		} else if (observable instanceof Stack) {
			p1 = ((Stack) observable).base().position();
		} else if (observable instanceof Corner) {
			p1 = ((Corner) observable).position();
		}
		if (p1 == null) {
			return null;
		}

		// Shape.
		if (landmark instanceof Shape) {
			Position p2 = ((Shape) landmark).position();
			return distance(p1.x(), p1.y(), p2.x(), p2.y());
		}

		// Stack.
		if (landmark instanceof Stack) {
			Position p2 = ((Stack) landmark).base().position();
			return distance(p1.x(), p1.y(), p2.x(), p2.y());
		}

		// Corner.
		if (landmark instanceof Corner) {
			Position p2 = ((Corner) landmark).position();
			return distance(p1.x(), p1.y(), p2.x(), p2.y());
		}

		// Edge.
		if (landmark instanceof Edge) {
			Edge edge = (Edge) landmark;
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
		if (landmark instanceof Region) {
			Region region = (Region) landmark;
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
		if (landmark instanceof Robot) {
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