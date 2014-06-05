/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.spatial;

import com.trainrobots.RoboticException;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class AdjacentDistribution extends SpatialDistribution {

	private final ObservableDistribution landmarkDistribution;

	public AdjacentDistribution(ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public double weight(Observable observable) {

		// Shape.
		if (observable instanceof Shape) {
			Shape shape = (Shape) observable;
			for (Observable landmark : landmarkDistribution) {

				// Shape.
				if (!(landmark instanceof Shape)) {
					throw new RoboticException(
							"Adjacent is not supported with landmark %s.",
							landmark);
				}
				Shape landmarkShape = (Shape) landmark;
				Position p1 = shape.position();
				Position p2 = landmarkShape.position();
				int dx = p2.x() - p1.x();
				int dy = p2.y() - p1.y();
				if (dx == 0 && (dy == 1 || dy == -1)) {
					return 1;
				}
				if (dy == 0 && (dx == 1 || dx == -1)) {
					return 1;
				}
			}
			return 0;
		}

		// Not supported.
		throw new RoboticException("Adjacent is not supported with %s.",
				observable);
	}
}