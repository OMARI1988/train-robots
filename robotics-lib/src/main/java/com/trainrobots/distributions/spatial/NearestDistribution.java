/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.spatial;

import com.trainrobots.RoboticException;
import com.trainrobots.distributions.Distance;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.distributions.observable.ObservableHypothesis;
import com.trainrobots.observables.Observable;

public class NearestDistribution extends SpatialDistribution {

	private final ObservableDistribution landmarkDistribution;

	public NearestDistribution(ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public double weight(Observable observable) {

		// Nearest.
		double best = 0;
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			Double weight = Distance.weight(observable, landmark);
			if (weight == null) {
				throw new RoboticException("%s nearest %s is not supported.",
						observable, landmark);
			}
			if (weight > best) {
				best = weight;
			}
		}
		return best;
	}
}