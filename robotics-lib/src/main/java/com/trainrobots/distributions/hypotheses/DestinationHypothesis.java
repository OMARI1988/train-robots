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
	private final double weight;

	public DestinationHypothesis(Position position, Observable landmark,
			double weight) {
		this.position = position;
		this.landmark = landmark;
		this.weight = weight;
	}

	public Position position() {
		return position;
	}

	public Observable landmark() {
		return landmark;
	}

	public double weight() {
		return weight;
	}

	@Override
	public String toString() {
		return weight + ": (" + position + ")";
	}
}