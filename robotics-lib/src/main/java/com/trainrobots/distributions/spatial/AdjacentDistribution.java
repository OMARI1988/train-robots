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
import com.trainrobots.distributions.observable.ObservableHypothesis;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Stack;
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
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();
			double weight = hypothesis.weight();

			// Shape/stack.
			if (observable instanceof Shape || observable instanceof Stack) {
				Position p1 = observable.referencePoint();

				// Shape/stack.
				if (landmark instanceof Shape || landmark instanceof Stack) {
					Position p2 = landmark.referencePoint();
					int dx = p2.x() - p1.x();
					int dy = p2.y() - p1.y();
					if (dx == 0 && (dy == 1 || dy == -1)) {
						return weight;
					}
					if (dy == 0 && (dx == 1 || dx == -1)) {
						return weight;
					}
					continue;
				}

				// Edge.
				if (landmark instanceof Edge) {
					if (((Edge) landmark).supports(p1)) {
						return weight;
					}
					continue;
				}
			}

			// Not supported.
			throw new RoboticException("%s adjacent %s is not supported.",
					observable, landmark);
		}
		return 0;
	}
}