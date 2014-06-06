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
			Position p1 = null;
			if (observable instanceof Shape) {
				p1 = ((Shape) observable).position();
			} else if (observable instanceof Stack) {
				p1 = ((Stack) observable).base().position();
			}
			if (p1 != null) {

				// Shape/stack.
				Position p2 = null;
				if (landmark instanceof Shape) {
					p2 = ((Shape) landmark).position();
				} else if (landmark instanceof Stack) {
					p2 = ((Stack) landmark).base().position();
				}
				if (p2 != null) {
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