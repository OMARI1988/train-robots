/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.spatial;

import com.trainrobots.RoboticException;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Region;
import com.trainrobots.observables.Robot;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class NearestDistribution extends SpatialDistribution {

	private final ObservableDistribution landmarkDistribution;

	public NearestDistribution(ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public double weight(Observable observable) {

		// Nearest.
		double best = 0;
		for (Observable landmark : landmarkDistribution) {
			Double distance = distance(observable, landmark);
			if (distance == null) {
				throw new RoboticException(
						"Nearest is not supported with landmark %s.", landmark);
			}
			double weight = 1 / distance;
			if (weight > best) {
				best = weight;
			}
		}
		return best;
	}

	private static Double distance(Observable observable, Observable landmark) {

		// Left-hand-side.
		if (!(observable instanceof Shape)) {
			return null;
		}
		Shape shape = (Shape) observable;
		Position p1 = shape.position();

		// Shape.
		if (landmark instanceof Shape) {
			Position p2 = ((Shape) landmark).position();
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