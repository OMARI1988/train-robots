/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.spatial;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.distributions.hypotheses.DestinationHypothesis;
import com.trainrobots.distributions.hypotheses.ObservableHypothesis;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.losr.Relations;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Stack;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

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

			// Shape/stack.
			Position p1 = null;
			if (observable instanceof Shape) {
				p1 = ((Shape) observable).position();
			} else if (observable instanceof Stack) {
				p1 = ((Stack) observable).base().position();
			}
			if (p1 != null) {

				// Shape/stack.
				Position p2 = null;
				if (landmark instanceof Shape) {
					p2 = ((Shape) landmark).position();
				} else if (landmark instanceof Stack) {
					p2 = ((Stack) landmark).base().position();
				}
				if (p2 != null) {
					int dx = p1.x() - p2.x();
					int dy = p1.y() - p2.y();
					if (dx == this.dx && dy == this.dy) {
						return 1;
					}
					continue;
				}
			}

			// Not supported.
			throw new RoboticException("%s %s %s is not supported.",
					observable, relation, landmark);
		}
		return 0;
	}

	public Items<DestinationHypothesis> destinations(PlannerContext context) {
		ItemsList<DestinationHypothesis> destinations = new ItemsList<DestinationHypothesis>();
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();

			// Shape/stack.
			Position p = null;
			if (landmark instanceof Shape) {
				p = ((Shape) landmark).position();
			} else if (landmark instanceof Stack) {
				p = ((Stack) landmark).base().position();
			}
			if (p == null) {
				throw new RoboticException(
						"%s destination is not supported with landmark %s.",
						relation, landmark);
			}
			destinations.add(new DestinationHypothesis(context.simulator()
					.dropPosition(p.add(dx, dy, 0)), landmark));
		}
		return destinations;
	}
}