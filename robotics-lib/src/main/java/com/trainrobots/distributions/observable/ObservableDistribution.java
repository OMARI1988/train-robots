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
import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Layout;

public abstract class ObservableDistribution extends Distribution implements
		Items<ObservableHypothesis> {

	private final List<ObservableHypothesis> hypotheses = new ArrayList<ObservableHypothesis>();

	protected ObservableDistribution(Layout layout) {
		super(layout);
	}

	@Override
	public int count() {
		return hypotheses.size();
	}

	@Override
	public ObservableHypothesis get(int index) {
		return hypotheses.get(index);
	}

	@Override
	public Iterator<ObservableHypothesis> iterator() {
		return hypotheses.iterator();
	}

	@Override
	public ObservableHypothesis[] toArray() {
		ObservableHypothesis[] array = new ObservableHypothesis[hypotheses
				.size()];
		hypotheses.toArray(array);
		return array;
	}

	@Override
	public String toString() {
		int count = count();
		return getClass().getSimpleName() + " (" + count + " "
				+ (count == 1 ? "hypothesis" : "hypotheses") + ")";
	}

	public Items<Observable> best() {

		// Best weight.
		double best = 0;
		for (ObservableHypothesis hypothesis : hypotheses) {
			double weight = hypothesis.weight();
			if (weight > best) {
				best = weight;
			}
		}

		// Select best hypotheses.
		ItemsList<Observable> result = new ItemsList<>();
		for (ObservableHypothesis hypothesis : hypotheses) {
			if (hypothesis.weight() == best) {
				result.add(hypothesis.observable());
			}
		}
		return result;
	}

	protected void normalize() {

		// Sum.
		double sum = 0;
		int size = hypotheses.size();
		for (int i = 0; i < size; i++) {
			sum += hypotheses.get(i).weight();
		}

		// Normalize.
		for (int i = 0; i < size; i++) {
			ObservableHypothesis hypothesis = hypotheses.get(i);
			hypothesis.weight(hypothesis.weight() / sum);
		}
	}

	protected void add(Observable observable, double weight) {
		add(new ObservableHypothesis(observable, weight));
	}

	protected void add(ObservableHypothesis hypothesis) {
		if (hypothesis.weight() != 0) {
			hypotheses.add(hypothesis);
		}
	}
}