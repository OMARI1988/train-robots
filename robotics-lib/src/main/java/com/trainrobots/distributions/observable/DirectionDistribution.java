/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Relations;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Stack;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public abstract class DirectionDistribution extends ObservableDistribution {

	protected DirectionDistribution(Relations relation,
			ObservableDistribution distribution) {
		super(distribution.layout());
		for (ObservableHypothesis hypothesis : distribution) {
			Observable observable = hypothesis.observable();
			double weight = hypothesis.weight();

			// Observable.
			if (observable instanceof Shape || observable instanceof Stack) {
				add(observable, weight * weight(observable.referencePoint()));
				continue;
			}

			// Not supported.
			throw new RoboticException("%s is not supported with %s.",
					relation, observable);
		}
		normalize();
	}

	protected abstract double weight(Position position);
}