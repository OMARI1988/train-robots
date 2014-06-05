/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.observables.Observable;

public class RelativeDistribution extends ObservableDistribution {

	public RelativeDistribution(ObservableDistribution landmarkDistribution,
			RelationDistribution relationDistribution) {
		super(landmarkDistribution.layout());

		// Weights.
		double best = 0;
		List<Hypothesis> hypotheses = new ArrayList<Hypothesis>();
		for (Observable observable : landmarkDistribution) {
			double weight = relationDistribution.weight(observable);
			if (weight == 0) {
				continue;
			}
			hypotheses.add(new Hypothesis(observable, weight));
			if (weight > best) {
				best = weight;
			}
		}

		// Select best hypotheses.
		for (Hypothesis hypothesis : hypotheses) {
			if (hypothesis.weight() == best) {
				add(hypothesis.observable());
			}
		}
	}
}