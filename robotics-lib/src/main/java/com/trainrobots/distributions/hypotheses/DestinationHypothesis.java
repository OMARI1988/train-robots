/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.hypotheses;

import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Position;

public class DestinationHypothesis {

	private final Position position;
	private final Observable landmark;

	public DestinationHypothesis(Position position, Observable landmark) {
		this.position = position;
		this.landmark = landmark;
	}

	public Position position() {
		return position;
	}

	public Observable landmark() {
		return landmark;
	}
}