/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.losr.Relations;
import com.trainrobots.scenes.Position;

public class LeftDistribution extends DirectionDistribution {

	public LeftDistribution(ObservableDistribution distribution) {
		super(Relations.Left, distribution);
	}

	@Override
	protected double weight(Position position) {
		return position.y();
	}
}