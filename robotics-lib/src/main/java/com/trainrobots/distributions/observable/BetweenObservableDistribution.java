/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Colors;
import com.trainrobots.scenes.Layout;

public class BetweenObservableDistribution extends ObservableDistribution {

	public BetweenObservableDistribution(Layout layout, Colors color1,
			Colors color2) {
		super(layout);

		// Not supported.
		throw new RoboticException(
				"Between is not supported as an observable distribution.");
	}
}