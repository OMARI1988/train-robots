/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.distributions.spatial.SpatialDistribution;
import com.trainrobots.observables.Observable;

public class RelativeDistribution extends ObservableDistribution {

	public RelativeDistribution(ObservableDistribution landmarkDistribution,
			SpatialDistribution spatialDistribution) {
		super(landmarkDistribution.layout());

		// Combine weights.
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable observable = hypothesis.observable();
			double weight = spatialDistribution.weight(observable);
			if (weight != 0) {
				add(observable, weight * hypothesis.weight());
			}
		}
	}
}