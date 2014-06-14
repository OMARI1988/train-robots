/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.distributions.Weights;
import com.trainrobots.losr.Indicators;
import com.trainrobots.scenes.Position;

public class CenterDistribution extends DirectionDistribution {

	public CenterDistribution(ObservableDistribution distribution) {
		super(Indicators.Center, distribution);
	}

	@Override
	protected double weight(Position position) {
		return Weights.center(position);
	}
}