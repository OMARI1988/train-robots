/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.observables.Observable;

public class ObservableHypothesis {

	private final Observable observable;
	private final double weight;

	public ObservableHypothesis(Observable observable, double weight) {
		this.observable = observable;
		this.weight = weight;
	}

	public Observable observable() {
		return observable;
	}

	public double weight() {
		return weight;
	}

	@Override
	public String toString() {
		return weight + ": " + observable;
	}
}