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
import com.trainrobots.observables.Board;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.ObservablePosition;
import com.trainrobots.observables.Region;
import com.trainrobots.scenes.Gripper;

public class PartDistribution extends SpatialDistribution {

	private final ObservableDistribution landmarkDistribution;

	public PartDistribution(ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public double weight(Observable observable) {
		for (ObservableHypothesis hypothesis : landmarkDistribution) {
			Observable landmark = hypothesis.observable();

			// Region/edge/corner of board.
			if ((observable instanceof Region || observable instanceof Edge || observable instanceof Corner)
					&& landmark instanceof Board) {
				return hypothesis.weight();
			}

			// Active position of gripper.
			if (observable == ObservablePosition.Active
					&& landmark instanceof Gripper) {
				return hypothesis.weight();
			}

			// Not supported.
			throw new RoboticException("%s part %s is not supported.",
					observable, landmark);
		}
		return 0;
	}
}