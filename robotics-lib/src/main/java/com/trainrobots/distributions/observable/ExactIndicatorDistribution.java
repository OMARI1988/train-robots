/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Indicators;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.ObservablePosition;
import com.trainrobots.observables.Region;

public class ExactIndicatorDistribution extends ObservableDistribution {

	public ExactIndicatorDistribution(ObservableDistribution distribution,
			Indicators indicator) {
		super(distribution.layout());

		// Observables.
		for (ObservableHypothesis hypothesis : distribution) {
			Observable observable = hypothesis.observable();

			// Edge.
			if (observable instanceof Edge) {
				Edge edge = (Edge) observable;
				if (edge.indicator() == indicator) {
					add(hypothesis);
				}
				continue;
			}

			// Region.
			if (observable instanceof Region) {
				Region region = (Region) observable;
				if (region.indicator() == indicator) {
					add(hypothesis);
				}
				continue;
			}

			// Corner.
			if (observable instanceof Corner) {
				Corner corner = (Corner) observable;
				if (corner.frontOrBack() == indicator
						|| corner.leftOrRight() == indicator) {
					add(hypothesis);
				}
				continue;
			}

			// Position.
			if (observable instanceof ObservablePosition
					&& indicator == Indicators.Active) {
				add(new ObservableHypothesis(ObservablePosition.Active,
						hypothesis.weight()));
				continue;
			}

			// Not supported.
			throw new RoboticException(
					"The indicator '%s' is not supported with %s.", indicator,
					observable);
		}
		normalize();
	}
}