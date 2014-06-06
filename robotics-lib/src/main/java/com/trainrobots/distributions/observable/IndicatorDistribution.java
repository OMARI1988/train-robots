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
import com.trainrobots.observables.Region;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class IndicatorDistribution extends ObservableDistribution {

	public IndicatorDistribution(ObservableDistribution distribution,
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

			// Shape.
			if (observable instanceof Shape
					&& indicator == Indicators.Individual) {
				Shape shape = (Shape) observable;
				Position p = shape.position();
				if (p.z() == 0 && layout.shape(p.add(0, 0, 1)) == null) {
					add(hypothesis);
				}
				continue;
			}

			// Not supported.
			throw new RoboticException(
					"The indicator '%s' is not supported with %s.", indicator,
					observable);
		}
	}
}