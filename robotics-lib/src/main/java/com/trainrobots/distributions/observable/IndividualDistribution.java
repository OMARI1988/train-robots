/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Types;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.ShapePair;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class IndividualDistribution extends ObservableDistribution {

	public IndividualDistribution(ObservableDistribution distribution) {
		super(distribution.layout());
		for (ObservableHypothesis hypothesis : distribution) {
			Observable observable = hypothesis.observable();

			// Shape.
			if (observable instanceof Shape) {
				if (individual(observable.referencePoint())) {
					add(hypothesis);
				}
				continue;
			}

			// Shape pair.
			if (observable instanceof ShapePair) {
				ShapePair shapePair = (ShapePair) observable;
				if (individual(shapePair.shape1().referencePoint())
						&& individual(shapePair.shape2().referencePoint())) {
					add(hypothesis);
				}
				continue;
			}

			// Not supported.
			throw new RoboticException("Individual is not supported with %s.",
					observable);
		}
		normalize();
	}

	private boolean individual(Position p) {
		if (p.z() != 0) {
			return false;
		}
		Shape shape = layout.shape(p.add(0, 0, 1));
		return shape == null || shape.type() == Types.Prism;
	}
}