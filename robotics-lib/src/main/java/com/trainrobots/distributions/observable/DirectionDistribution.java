/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Indicators;
import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Position;

public abstract class DirectionDistribution extends ObservableDistribution {

	protected DirectionDistribution(Indicators indicator,
			ObservableDistribution distribution) {
		super(distribution.layout());
		for (ObservableHypothesis hypothesis : distribution) {
			Observable observable = hypothesis.observable();
			double weight = hypothesis.weight();

			// Observable.
			Position p = observable.referencePoint();
			if (p != null) {
				add(observable, weight * weight(p));
				continue;
			}

			// Not supported.
			throw new RoboticException("%s is not supported with %s.",
					indicator, observable);
		}
		normalize();
	}

	protected abstract double weight(Position position);
}