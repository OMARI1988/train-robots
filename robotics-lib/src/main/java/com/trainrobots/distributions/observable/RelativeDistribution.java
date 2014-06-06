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

public class RelativeDistribution extends ObservableDistribution {

	public RelativeDistribution(ObservableDistribution landmarkDistribution,
			SpatialDistribution spatialDistribution) {
		super(landmarkDistribution.layout());

		// Weights.
		double best = 0;
		List<ObservableHypothesis> result = new ArrayList<ObservableHypothesis>();
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			double weight = spatialDistribution.weight(hypothesis.observable());
			if (weight == 0) {
				continue;
			}
			// TODO: Multiply weights?
			result.add(new ObservableHypothesis(hypothesis.observable(), weight));
			if (weight > best) {
				best = weight;
			}
		}

		// Select best hypotheses.
		for (ObservableHypothesis hypothesis : result) {
			if (hypothesis.weight() == best) {
				add(hypothesis);
			}
		}
	}
}