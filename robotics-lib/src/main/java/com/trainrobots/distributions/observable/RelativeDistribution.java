/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.distributions.hypotheses.ObservableHypothesis;
import com.trainrobots.distributions.spatial.SpatialDistribution;
import com.trainrobots.observables.Observable;

public class RelativeDistribution extends ObservableDistribution {

	public RelativeDistribution(ObservableDistribution landmarkDistribution,
			SpatialDistribution spatialDistribution) {
		super(landmarkDistribution.layout());

		// Weights.
		double best = 0;
		List<ObservableHypothesis> hypotheses = new ArrayList<ObservableHypothesis>();
		for (Observable observable : landmarkDistribution) {
			double weight = spatialDistribution.weight(observable);
			if (weight == 0) {
				continue;
			}
			hypotheses.add(new ObservableHypothesis(observable, weight));
			if (weight > best) {
				best = weight;
			}
		}

		// Select best hypotheses.
		for (ObservableHypothesis hypothesis : hypotheses) {
			if (hypothesis.weight() == best) {
				add(hypothesis.observable());
			}
		}
	}
}