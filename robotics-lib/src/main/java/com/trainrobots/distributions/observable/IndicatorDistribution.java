/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.distributions.hypotheses.ObservableHypothesis;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Indicators;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Region;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class IndicatorDistribution extends ObservableDistribution {

	public IndicatorDistribution(ObservableDistribution distribution,
			Items<Indicator> indicators) {
		super(distribution.layout());

		// Indicator.
		int size = indicators.count();

		// Observables.
		for (ObservableHypothesis hypothesis : distribution) {
			Observable observable = hypothesis.observable();

			// Edge.
			if (observable instanceof Edge) {
				if (size == 1) {
					Indicators indicator = indicators.get(0).indicator();
					Edge edge = (Edge) observable;
					if (edge.indicator() == indicator) {
						add(hypothesis);
					}
				}
				continue;
			}

			// Region.
			if (observable instanceof Region) {
				if (size == 1) {
					Indicators indicator = indicators.get(0).indicator();
					Region region = (Region) observable;
					if (region.indicator() == indicator) {
						add(hypothesis);
					}
				}
				continue;
			}

			// Corner.
			if (observable instanceof Corner) {
				Corner corner = (Corner) observable;
				boolean match = true;
				for (int i = 0; i < size; i++) {
					Indicators indicator = indicators.get(i).indicator();
					match &= corner.frontOrBack() == indicator
							|| corner.leftOrRight() == indicator;
				}
				if (match) {
					add(hypothesis);
				}
				continue;
			}

			// Shape.
			if (observable instanceof Shape && size == 1
					&& indicators.get(0).indicator() == Indicators.Individual) {
				Shape shape = (Shape) observable;
				Position p = shape.position();
				if (p.z() == 0 && layout.shape(p.add(0, 0, 1)) == null) {
					add(hypothesis);
				}
				continue;
			}

			// Not supported.
			StringBuilder text = new StringBuilder();
			text.append("The indicator '");
			for (int i = 0; i < size; i++) {
				if (i > 0) {
					text.append(' ');
				}
				text.append(indicators.get(i).indicator());
			}
			text.append("' is not supported with ");
			text.append(observable);
			text.append('.');
			throw new RoboticException(text.toString());
		}
	}
}