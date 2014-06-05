/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.observables.Observable;

public class RelativeDistribution extends ObservableDistribution {

	public RelativeDistribution(ObservableDistribution landmarkDistribution,
			RelationDistribution relationDistribution) {
		super(landmarkDistribution.layout());
		for (Observable observable : landmarkDistribution) {
			if (relationDistribution.matches(observable)) {
				add(observable);
			}
		}
	}
}