/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.distributions.Distribution;
import com.trainrobots.distributions.hypotheses.ObservableHypothesis;
import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Layout;

public abstract class ObservableDistribution extends Distribution implements
		Items<ObservableHypothesis> {

	private final List<ObservableHypothesis> observables = new ArrayList<ObservableHypothesis>();

	protected ObservableDistribution(Layout layout) {
		super(layout);
	}

	@Override
	public int count() {
		return observables.size();
	}

	@Override
	public ObservableHypothesis get(int index) {
		return observables.get(index);
	}

	@Override
	public Iterator<ObservableHypothesis> iterator() {
		return observables.iterator();
	}

	@Override
	public ObservableHypothesis[] toArray() {
		ObservableHypothesis[] array = new ObservableHypothesis[observables
				.size()];
		observables.toArray(array);
		return array;
	}

	@Override
	public String toString() {
		int count = count();
		return getClass().getSimpleName() + " (" + count + " "
				+ (count == 1 ? "observable" : "observables") + ")";
	}

	public Items<Observable> best() {

		// Best weight.
		double best = 0;
		for (ObservableHypothesis hypothesis : observables) {
			double weight = hypothesis.weight();
			if (weight > best) {
				best = weight;
			}
		}

		// Select best hypotheses.
		ItemsList<Observable> result = new ItemsList<>();
		for (ObservableHypothesis hypothesis : observables) {
			if (hypothesis.weight() == best) {
				result.add(hypothesis.observable());
			}
		}
		return result;
	}

	protected void add(Observable observable, double weight) {
		add(new ObservableHypothesis(observable, weight));
	}

	protected void add(ObservableHypothesis observable) {
		observables.add(observable);
	}
}