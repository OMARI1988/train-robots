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

public class BackDistribution extends DirectionDistribution {

	public BackDistribution(ObservableDistribution distribution) {
		super(Relations.Back, distribution);
	}

	@Override
	protected double weight(Position position) {
		return 7 - position.x();
	}
}