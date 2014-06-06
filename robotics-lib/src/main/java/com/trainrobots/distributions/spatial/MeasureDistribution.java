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
import com.trainrobots.collections.SingleItem;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.distributions.observable.ObservableHypothesis;
import com.trainrobots.losr.Relations;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Stack;
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

		// No landmarks?
		if (landmarkDistribution == null) {
			return new SingleItem(destination(context, null, 1));
		}

		// Landmarks.
		ItemsList<DestinationHypothesis> destinations = new ItemsList<DestinationHypothesis>();
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			double weight = hypothesis.weight();
			destinations.add(destination(context, landmark, weight));
		}
		return destinations;
	}

	private DestinationHypothesis destination(PlannerContext context,
			Observable landmark, double weight) {

		// No landmark?
		Position position;
		if (landmark == null) {
			Shape sourceShape = context.sourceShape();
			if (context.sourceShape() == null) {
				throw new RoboticException("Source shape not specified.");
			}
			position = sourceShape.position();
		}

		// Shape.
		else if (landmark instanceof Shape) {
			position = ((Shape) landmark).position();
		}

		// Stack.
		else if (landmark instanceof Stack) {
			position = ((Stack) landmark).base().position();
		}

		// Corner.
		else if (landmark instanceof Corner) {
			position = ((Corner) landmark).position();
		}

		// Not supported.
		else {
			throw new RoboticException(
					"Measures with landmark %s are not supported.", landmark);
		}

		// Offset.
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
		return new DestinationHypothesis(position, null, weight);
	}
}