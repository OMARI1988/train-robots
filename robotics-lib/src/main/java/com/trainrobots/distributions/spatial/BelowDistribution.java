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
import com.trainrobots.observables.ObservablePosition;
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

			// Active position.
			if (landmark == ObservablePosition.Active && p1 != null) {
				Position p2 = layout.gripper().position();
				if (p1.x() == p2.x() && p1.y() == p2.y()) {
					return weight;
				}
				continue;
			}

			// Gripper.
			if (landmark instanceof Gripper) {
				Gripper gripper = (Gripper) landmark;
				Position p2 = gripper.position();
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

			// Active position.
			if (landmark == ObservablePosition.Active) {
				destinations.add(new DestinationHypothesis((context.simulator()
						.dropPosition(layout.gripper().position())), null,
						weight));
			}

			// Gripper.
			else if (landmark instanceof Gripper) {
				Gripper gripper = (Gripper) landmark;
				destinations.add(new DestinationHypothesis((context.simulator()
						.dropPosition(gripper.position())), null, weight));
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