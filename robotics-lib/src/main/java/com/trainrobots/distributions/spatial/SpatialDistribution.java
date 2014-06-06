/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.spatial;

import com.trainrobots.RoboticException;
import com.trainrobots.distributions.Distribution;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.losr.Relations;
import com.trainrobots.observables.Observable;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Layout;

public abstract class SpatialDistribution extends Distribution {

	protected SpatialDistribution(Layout layout) {
		super(layout);
	}

	public double weight(Observable observable) {
		throw new RoboticException("%s does not support weights.", getClass()
				.getSimpleName());
	}

	public DestinationDistribution destinations(PlannerContext context) {
		throw new RoboticException("%s does not support destinations.",
				getClass().getSimpleName());
	}

	public static SpatialDistribution of(Relations relation,
			ObservableDistribution landmarkDistribution) {

		// Relation.
		switch (relation) {
		case Left:
			return new LeftOffsetDistribution(landmarkDistribution);
		case Right:
			return new RightOffsetDistribution(landmarkDistribution);
		case Front:
			return new FrontOffsetDistribution(landmarkDistribution);
		case Within:
		case Above:
			return new AboveDistribution(landmarkDistribution);
		case Adjacent:
			return new AdjacentDistribution(landmarkDistribution);
		case Nearest:
			return new NearestDistribution(landmarkDistribution);
		}

		// Not supported.
		throw new RoboticException(
				"The relation '%s' is not supported as a spatial distribution.",
				relation);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}