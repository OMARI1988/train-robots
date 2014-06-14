/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.RoboticException;
import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class ActiveDistribution extends ObservableDistribution {

	public ActiveDistribution(ObservableDistribution distribution) {
		super(distribution.layout());
		for (ObservableHypothesis hypothesis : distribution) {
			Observable observable = hypothesis.observable();

			// Shape.
			if (observable instanceof Shape) {
				Position p1 = observable.referencePoint();
				Position p2 = layout.gripper().position();
				if (p1.x() == p2.x() && p1.y() == p2.y()) {
					add(hypothesis);
				}
				continue;
			}

			// Not supported.
			throw new RoboticException("Active is not supported with %s.",
					observable);
		}
		normalize();
	}
}