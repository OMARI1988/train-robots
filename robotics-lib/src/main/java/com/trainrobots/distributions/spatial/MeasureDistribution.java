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
import com.trainrobots.observables.Observable;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class MeasureDistribution extends SpatialDistribution {

	private final int cardinal;
	private final Indicators indicator;
	private final ObservableDistribution landmarkDistribution;

	public MeasureDistribution(Layout layout, int cardinal,
			Indicators indicator, ObservableDistribution landmarkDistribution) {
		super(layout);
		this.cardinal = cardinal;
		this.indicator = indicator;
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public DestinationDistribution destinations(PlannerContext context) {

		// No landmarks?
		DestinationDistribution destinations = new DestinationDistribution(
				layout);
		if (landmarkDistribution == null) {
			destinations.add(destination(context, null, 1));
			return destinations;
		}

		// Landmarks.
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			double weight = hypothesis.weight();
			destinations.add(destination(context, landmark, weight));
		}
		destinations.normalize();
		return destinations;
	}

	private DestinationHypothesis destination(PlannerContext context,
			Observable landmark, double weight) {

		// Use source shape if landmark not specified.
		if (landmark == null) {
			Shape sourceShape = context.sourceShape();
			if (context.sourceShape() == null) {
				throw new RoboticException("Source shape not specified.");
			}
			landmark = sourceShape;
		}

		// Position.
		Position position = landmark.referencePoint();
		if (position == null) {
			throw new RoboticException(
					"Measures with landmark %s are not supported.", landmark);
		}

		// Offset.
		switch (indicator) {
		case Left:
			position = position.add(0, cardinal, 0);
			break;
		case Right:
			position = position.add(0, -cardinal, 0);
			break;
		case Forward:
			position = position.add(cardinal, 0, 0);
			break;
		case Backward:
			position = position.add(-cardinal, 0, 0);
			break;
		default:
			throw new RoboticException(
					"The measure indicator '%s' is not supported.", indicator);
		}
		position = context.simulator().dropPosition(position);
		return new DestinationHypothesis(position, null, weight);
	}
}