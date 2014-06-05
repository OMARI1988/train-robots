/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.spatial;

import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.losr.Relations;

public class FrontDistribution extends OffsetDistribution {

	public FrontDistribution(ObservableDistribution landmarkDistribution) {
		super(Relations.Front, landmarkDistribution, 1, 0);
	}
}