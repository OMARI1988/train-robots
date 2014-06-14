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
import com.trainrobots.observables.ActivePosition;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Stack;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Gripper;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class BelowDistribution extends SpatialDistribution {

	private final ObservableDistribution landmarkDistribution;

	public BelowDistribution(ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public double weight(Observable observable) {

		// Shape/stack.
		if (!(observable instanceof Shape || observable instanceof Stack)) {
			return 0;
		}
		Position p1 = observable.referencePoint();

		// Landmarks.
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			double weight = hypothesis.weight();

			// Gripper / active position.
			if (landmark instanceof Gripper
					|| landmark instanceof ActivePosition) {
				Position p2 = landmark.referencePoint();
				if (p1.x() == p2.x() && p1.y() == p2.y()) {
					return weight;
				}
				continue;
			}

			// Not supported.
			throw new RoboticException("%s below %s is not supported.",
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

			// Gripper / active position.
			if (landmark instanceof Gripper
					|| landmark instanceof ActivePosition) {
				destinations
						.add(new DestinationHypothesis((context.simulator()
								.dropPosition(landmark.referencePoint())),
								null, weight));
			}

			// Not supported.
			else {
				throw new RoboticException(
						"Below destination is not supported with landmark %s.",
						landmark);
			}
		}
		destinations.normalize();
		return destinations;
	}
}