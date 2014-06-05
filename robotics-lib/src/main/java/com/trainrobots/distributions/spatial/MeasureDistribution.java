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
import com.trainrobots.collections.SingleItem;
import com.trainrobots.distributions.hypotheses.DestinationHypothesis;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.losr.Relations;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class MeasureDistribution extends SpatialDistribution {

	private final int tileCount;
	private final Relations relation;
	private final ObservableDistribution landmarkDistribution;

	public MeasureDistribution(Layout layout, int tileCount,
			Relations relation, ObservableDistribution landmarkDistribution) {
		super(layout);
		this.tileCount = tileCount;
		this.relation = relation;
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public Items<DestinationHypothesis> destinations(PlannerContext context) {

		// Landmark?
		if (landmarkDistribution != null) {
			throw new RoboticException(
					"Landmarks with measures are not supported.");
		}

		// Source shape.
		Shape sourceShape = context.sourceShape();
		if (context.sourceShape() == null) {
			throw new RoboticException("Source shape not specified.");
		}

		// Offset.
		Position position = sourceShape.position();
		switch (relation) {
		case Left:
			position = position.add(0, tileCount, 0);
			break;
		case Right:
			position = position.add(0, -tileCount, 0);
			break;
		case Forward:
			position = position.add(tileCount, 0, 0);
			break;
		case Backward:
			position = position.add(-tileCount, 0, 0);
			break;
		default:
			throw new RoboticException(
					"The measure relation '%s' is not supported.");
		}
		position = context.simulator().dropPosition(position);
		return new SingleItem(new DestinationHypothesis(position, null));
	}
}