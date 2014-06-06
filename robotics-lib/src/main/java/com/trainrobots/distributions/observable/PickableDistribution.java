/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Shape;

public class PickableDistribution extends ObservableDistribution {

	public PickableDistribution(ObservableDistribution distribution) {
		super(distribution.layout());
		for (ObservableHypothesis hypothesis : distribution) {
			if (pickable(hypothesis.observable())) {
				add(hypothesis);
			}
		}
	}

	private boolean pickable(Observable observable) {

		// Only shapes that do not support other shapes are pickable.
		if (!(observable instanceof Shape)) {
			return false;
		}
		Shape shape = (Shape) observable;
		return layout.shape(shape.position().add(0, 0, 1)) == null;
	}
}