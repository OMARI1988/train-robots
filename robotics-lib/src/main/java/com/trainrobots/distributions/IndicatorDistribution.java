/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Indicators;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Region;

public class IndicatorDistribution extends ObservableDistribution {

	public IndicatorDistribution(ObservableDistribution distribution,
			Items<Indicator> indicators) {
		super(distribution.layout());

		// Indicator.
		int size = indicators.count();

		// Observables.
		for (Observable observable : distribution) {

			// Edge.
			if (observable instanceof Edge) {
				if (size == 1) {
					Indicators indicator = indicators.get(0).indicator();
					Edge edge = (Edge) observable;
					if (edge.indicator() == indicator) {
						add(edge);
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
						add(region);
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
					add(corner);
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