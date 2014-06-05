/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.spatial;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.distributions.hypotheses.DestinationHypothesis;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Shape;

public class DropDestinationDistribution extends SpatialDistribution {

	private final SpatialDistribution distribution;

	public DropDestinationDistribution(SpatialDistribution distribution) {
		super(distribution.layout());
		this.distribution = distribution;
	}

	@Override
	public Items<DestinationHypothesis> destinations(PlannerContext context) {
		ItemsList<DestinationHypothesis> result = new ItemsList<DestinationHypothesis>();
		Shape sourceShape = context.sourceShape();
		for (DestinationHypothesis destination : distribution
				.destinations(context)) {

			// Destination must be clear.
			if (layout.shape(destination.position()) == null) {

				// Ignore whatever is being moved.
				if (sourceShape != null
						&& sourceShape.equals(destination.landmark())) {
					continue;
				}
				result.add(destination);
			}
		}
		return result;
	}
}