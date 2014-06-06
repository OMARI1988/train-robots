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

public class DroppableDistribution extends ObservableDistribution {

	public DroppableDistribution(ObservableDistribution distribution) {
		super(distribution.layout());
		for (ObservableHypothesis hypothesis : distribution) {
			if (droppable(hypothesis.observable())) {
				add(hypothesis);
			}
		}
		normalize();
	}

	private boolean droppable(Observable observable) {

		// Only the shape in the gripper is droppable.
		if (!(observable instanceof Shape)) {
			return false;
		}
		Shape shape = (Shape) observable;
		return shape.equals(layout.gripper().shape());
	}
}