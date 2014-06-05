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
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.losr.Indicators;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Region;
import com.trainrobots.observables.Stack;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class WithinDistribution extends SpatialDistribution {

	private final ObservableDistribution landmarkDistribution;

	public WithinDistribution(ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public double weight(Observable observable) {
		for (Observable landmark : landmarkDistribution) {

			// Corner.
			if (landmark instanceof Corner) {
				Corner corner = (Corner) landmark;
				Position p1 = null;
				if (observable instanceof Shape) {
					p1 = ((Shape) observable).position();
				} else if (observable instanceof Stack) {
					p1 = ((Stack) observable).base().position();
				}
				if (p1 != null) {
					Position p2 = corner.position();
					if (p1.x() == p2.x() && p1.y() == p2.y()) {
						return 1;
					}
					continue;
				}
			}

			// Region.
			if (landmark instanceof Region) {
				Region region = (Region) landmark;
				if (region.indicator() == Indicators.Center) {
					Position p1 = null;
					if (observable instanceof Shape) {
						p1 = ((Shape) observable).position();
					} else if (observable instanceof Stack) {
						p1 = ((Stack) observable).base().position();
					}
					if (p1 != null) {
						if ((p1.x() == 3 || p1.x() == 4)
								&& (p1.y() == 3 || p1.y() == 4)) {
							return 1;
						}
						continue;
					}
				}
			}

			// Not supported.
			throw new RoboticException("%s within %s is not supported.",
					observable, landmark);
		}
		return 0;
	}

	@Override
	public Items<DestinationHypothesis> destinations(PlannerContext context) {
		ItemsList<DestinationHypothesis> destinations = new ItemsList<DestinationHypothesis>();
		for (Observable landmark : landmarkDistribution) {

			// Corner.
			if (landmark instanceof Corner) {
				Corner corner = (Corner) landmark;
				destinations.add(new DestinationHypothesis(context.simulator()
						.dropPosition(corner.position()), corner));
			}

			// Not supported.
			else {
				throw new RoboticException(
						"Within destination is not supported with landmark %s.",
						landmark);
			}
		}
		return destinations;
	}
}