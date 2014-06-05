/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Relations;
import com.trainrobots.observables.Board;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Region;
import com.trainrobots.observables.Robot;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class RelationDistribution extends Distribution {

	private final Relations relation;
	private final ObservableDistribution landmarkDistribution;

	public RelationDistribution(Relations relation,
			ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());
		this.relation = relation;
		this.landmarkDistribution = landmarkDistribution;
	}

	public double weight(Observable observable) {

		// Above.
		if (relation == Relations.Above && observable instanceof Shape) {
			Shape shape = (Shape) observable;
			for (Observable landmark : landmarkDistribution) {
				if (landmark instanceof Shape) {
					Shape landmarkShape = (Shape) landmark;
					if (landmarkShape.position().add(0, 0, 1)
							.equals(shape.position())) {
						return 1;
					}
				} else if (landmark instanceof Board) {
					if (shape.position().z() == 0) {
						return 1;
					}
				} else if (landmark instanceof Edge) {
					Edge edge = (Edge) landmark;
					switch (edge.indicator()) {
					case Left:
						if (shape.position().y() == 7) {
							return 1;
						}
						break;
					case Right:
						if (shape.position().y() == 0) {
							return 1;
						}
						break;
					case Front:
						if (shape.position().x() == 7) {
							return 1;
						}
						break;
					case Back:
						if (shape.position().x() == 0) {
							return 1;
						}
						break;
					}
				} else {
					throw new RoboticException(
							"The relation '%s' is not supported with landmark %s.",
							relation, landmark);
				}
			}
			return 0;
		}

		// Within.
		if (relation == Relations.Within && observable instanceof Shape) {
			Shape shape = (Shape) observable;
			for (Observable landmark : landmarkDistribution) {
				if (landmark instanceof Corner) {
					Corner corner = (Corner) landmark;
					if (corner.position().add(0, 0, 1).equals(shape.position())) {
						return 1;
					}
				} else {
					throw new RoboticException(
							"The relation '%s' is not supported with landmark %s.",
							relation, landmark);
				}
			}
			return 0;
		}

		// Adjacent.
		if (relation == Relations.Adjacent && observable instanceof Shape) {
			Shape shape = (Shape) observable;
			for (Observable landmark : landmarkDistribution) {
				if (!(landmark instanceof Shape)) {
					throw new RoboticException(
							"The relation '%s' is not supported with landmark %s.",
							relation, landmark);
				}
				Shape landmarkShape = (Shape) landmark;
				Position p1 = shape.position();
				Position p2 = landmarkShape.position();
				int dx = p2.x() - p1.x();
				int dy = p2.y() - p1.y();
				if (dx == 0 && (dy == 1 || dy == -1)) {
					return 1;
				}
				if (dy == 0 && (dx == 1 || dx == -1)) {
					return 1;
				}
			}
			return 0;
		}

		// Nearest.
		if (relation == Relations.Nearest) {
			double best = 0;
			for (Observable landmark : landmarkDistribution) {
				Double distance = distance(observable, landmark);
				if (distance == null) {
					throw new RoboticException(
							"The relation '%s' is not supported with landmark %s.",
							relation, landmark);
				}
				double weight = 1 / distance;
				if (weight > best) {
					best = weight;
				}
			}
			return best;
		}

		// Not supported.
		throw new RoboticException(
				"The relation '%s' is not supported with %s.", relation,
				observable);
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