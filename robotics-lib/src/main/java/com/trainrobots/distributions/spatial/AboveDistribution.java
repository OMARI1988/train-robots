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
import com.trainrobots.distributions.observable.ObservableHypothesis;
import com.trainrobots.losr.Indicators;
import com.trainrobots.observables.Board;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Region;
import com.trainrobots.observables.Stack;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class AboveDistribution extends SpatialDistribution {

	private final ObservableDistribution landmarkDistribution;

	public AboveDistribution(ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public double weight(Observable observable) {

		// Observable.
		Position p1 = null;
		if (observable instanceof Shape) {
			p1 = ((Shape) observable).position();
		} else if (observable instanceof Stack) {
			p1 = ((Stack) observable).base().position();
		}

		// Landmarks.
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			double weight = hypothesis.weight();

			// Shape.
			if (landmark instanceof Shape && observable instanceof Shape) {
				if (((Shape) landmark).position().add(0, 0, 1).equals(p1)) {
					return weight;
				}
				continue;
			}

			// Corner.
			if (landmark instanceof Corner && p1 != null) {
				Position p2 = ((Corner) landmark).position();
				if (p1.x() == p2.x() && p1.y() == p2.y()) {
					return weight;
				}
				continue;
			}

			// Board.
			if (landmark instanceof Board && observable instanceof Shape) {
				return weight * (7 - p1.z());
			}

			// Edge.
			if (landmark instanceof Edge && p1 != null) {
				if (((Edge) landmark).supports(p1)) {
					return weight;
				}
				continue;
			}

			// Stack.
			if (landmark instanceof Stack && observable instanceof Shape) {
				Stack stack = (Stack) landmark;
				if (stack.top().position().add(0, 0, 1).equals(p1)) {
					return weight;
				}
				continue;
			}

			// Edge above region.
			if (landmark instanceof Region && observable instanceof Edge) {
				if (((Region) landmark).indicator() == ((Edge) observable)
						.indicator()) {
					return weight;
				}
				continue;
			}

			// Shape/stake above region.
			if (landmark instanceof Region && p1 != null) {
				Region region = (Region) landmark;

				// Center.
				if (region.indicator() == Indicators.Center) {
					if ((p1.x() == 3 || p1.x() == 4)
							&& (p1.y() == 3 || p1.y() == 4)) {
						return weight;
					}
					continue;
				}

				// Left.
				if (region.indicator() == Indicators.Left) {
					return weight * p1.y();
				}

				// Right.
				if (region.indicator() == Indicators.Right) {
					return weight * (7 - p1.y());
				}

				// Front.
				if (region.indicator() == Indicators.Front) {
					return weight * (7 - p1.x());
				}

				// Back.
				if (region.indicator() == Indicators.Back) {
					return weight * p1.x();
				}
			}

			// Not supported.
			throw new RoboticException("%s above %s is not supported.",
					observable, landmark);
		}
		return 0;
	}

	@Override
	public DestinationDistribution destinations(PlannerContext context) {
		DestinationDistribution destinations = new DestinationDistribution(
				layout);
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			double weight = hypothesis.weight();

			// Shape.
			if (landmark instanceof Shape) {
				Shape landmarkShape = (Shape) landmark;
				destinations.add(new DestinationHypothesis(landmarkShape
						.position().add(0, 0, 1), landmarkShape, weight));
			}

			// Corner.
			else if (landmark instanceof Corner) {
				Corner corner = (Corner) landmark;
				destinations.add(new DestinationHypothesis(context.simulator()
						.dropPosition(corner.position()), corner, weight));
			}

			// Stack.
			else if (landmark instanceof Stack) {
				Stack stack = (Stack) landmark;
				destinations.add(new DestinationHypothesis(stack.top()
						.position().add(0, 0, 1), stack, weight));
			}

			// Board.
			else if (landmark instanceof Board && context.sourceShape() != null) {
				Shape shape = context.sourceShape();
				destinations.add(new DestinationHypothesis(context.simulator()
						.dropPosition(shape.position()), null, weight));
			}

			// Not supported.
			else {
				throw new RoboticException(
						"Above destination is not supported with landmark %s.",
						landmark);
			}
		}
		destinations.normalize();
		return destinations;
	}
}