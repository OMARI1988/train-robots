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
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.ShapePair;
import com.trainrobots.observables.Stack;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class BetweenDistribution extends SpatialDistribution {

	private final ObservableDistribution landmarkDistribution;

	public BetweenDistribution(ObservableDistribution landmarkDistribution) {
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

			// Shape pair.
			if (landmark instanceof ShapePair && p1 != null) {
				if (((ShapePair) landmark).midPoint().equals(p1)) {
					return weight;
				}
				continue;
			}

			// Not supported.
			throw new RoboticException("%s between %s is not supported.",
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

			// Shape pair.
			if (landmark instanceof ShapePair) {
				ShapePair shapePair = (ShapePair) landmark;
				destinations.add(new DestinationHypothesis(
						shapePair.midPoint(), null, weight));
				continue;
			}

			// Not supported.
			throw new RoboticException(
					"Between destination is not supported with landmark %s.",
					landmark);
		}
		destinations.normalize();
		return destinations;
	}
}