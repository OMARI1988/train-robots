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
import com.trainrobots.observables.ActivePosition;
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
		if (observable instanceof Shape || observable instanceof Stack) {
			p1 = observable.referencePoint();
		}

		// Landmarks.
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			double weight = hypothesis.weight();

			// Shape/stack.
			if ((landmark instanceof Shape || landmark instanceof Stack)
					&& p1 != null) {
				if (landmark.referencePoint().add(0, 0, 1).equals(p1)) {
					return weight;
				}
				continue;
			}

			// Corner / active position.
			if ((landmark instanceof Corner || landmark instanceof ActivePosition)
					&& p1 != null) {
				Position p2 = landmark.referencePoint();
				if (p1.x() == p2.x() && p1.y() == p2.y()) {
					return weight;
				}
				continue;
			}

			// Board.
			if (landmark instanceof Board && observable instanceof Shape
					&& p1 != null) {
				return weight * (7 - p1.z());
			}

			// Edge.
			if (landmark instanceof Edge && p1 != null) {
				if (((Edge) landmark).supports(p1)) {
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

			// Shape/stack above region.
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

			// Shape/stack.
			if (landmark instanceof Shape || landmark instanceof Stack) {
				destinations.add(new DestinationHypothesis(landmark
						.referencePoint().add(0, 0, 1), landmark, weight));
				continue;
			}

			// Corner / active position.
			if (landmark instanceof Corner
					|| landmark instanceof ActivePosition) {
				destinations
						.add(new DestinationHypothesis(context.simulator()
								.dropPosition(landmark.referencePoint()), null,
								weight));
				continue;
			}

			// Board.
			if (landmark instanceof Board && context.sourceShape() != null) {
				Shape shape = context.sourceShape();
				destinations.add(new DestinationHypothesis(context.simulator()
						.dropPosition(shape.position()), null, weight));
				continue;
			}

			// Not supported.
			throw new RoboticException(
					"Above destination is not supported with landmark %s.",
					landmark);
		}
		destinations.normalize();
		return destinations;
	}
}