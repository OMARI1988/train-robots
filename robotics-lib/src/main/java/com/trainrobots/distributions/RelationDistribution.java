/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Relations;
import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Shape;

public class RelationDistribution extends Distribution {

	private ObservableDistribution landmarkDistribution;

	public RelationDistribution(Relations relation,
			ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());

		// Above?
		if (relation == Relations.Above) {
			this.landmarkDistribution = landmarkDistribution;
			return;
		}

		// Not supported.
		throw new RoboticException("The relation '%s' is not supported.",
				relation);
	}

	public boolean matches(Observable observable) {

		// Shape?
		if (!(observable instanceof Shape)) {
			return false;
		}
		Shape shape = (Shape) observable;

		// Above.
		for (Observable landmark : landmarkDistribution) {
			if (landmark instanceof Shape) {
				Shape landmarkShape = (Shape) landmark;
				if (landmarkShape.position().add(0, 0, 1)
						.equals(shape.position())) {
					return true;
				}
			}
		}
		return false;
	}
}