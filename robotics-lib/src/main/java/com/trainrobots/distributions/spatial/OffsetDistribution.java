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
import com.trainrobots.losr.Relations;
import com.trainrobots.observables.Observable;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Position;

public abstract class OffsetDistribution extends SpatialDistribution {

	private final Relations relation;
	private final ObservableDistribution landmarkDistribution;
	private final int dx;
	private final int dy;

	protected OffsetDistribution(Relations relation,
			ObservableDistribution landmarkDistribution, int dx, int dy) {
		super(landmarkDistribution.layout());
		this.relation = relation;
		this.landmarkDistribution = landmarkDistribution;
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public double weight(Observable observable) {
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			double weight = hypothesis.weight();

			// Shape/stack.
			Position p1 = observable.referencePoint();
			Position p2 = landmark.referencePoint();
			if (p1 != null && p2 != null) {
				int dx = p1.x() - p2.x();
				int dy = p1.y() - p2.y();
				if (dx == this.dx && dy == this.dy) {
					return weight;
				}
			}

			// Not supported.
			else {
				throw new RoboticException("%s %s %s is not supported.",
						observable, relation, landmark);
			}
		}
		return 0;
	}

	public DestinationDistribution destinations(PlannerContext context) {
		DestinationDistribution destinations = new DestinationDistribution(
				layout);
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			double weight = hypothesis.weight();

			// Shape/stack.
			Position p = landmark.referencePoint();
			if (p != null) {
				destinations.add(new DestinationHypothesis(context.simulator()
						.dropPosition(p.add(dx, dy, 0)), landmark, weight));
			}

			// Not supported.
			else {
				throw new RoboticException(
						"%s destination is not supported with landmark %s.",
						relation, landmark);
			}
		}
		destinations.normalize();
		return destinations;
	}
}