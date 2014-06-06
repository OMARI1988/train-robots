/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.spatial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.distributions.Distribution;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;

public class DestinationDistribution extends Distribution implements
		Items<DestinationHypothesis> {

	private final List<DestinationHypothesis> hypotheses = new ArrayList<DestinationHypothesis>();

	public DestinationDistribution(Layout layout) {
		super(layout);
	}

	@Override
	public int count() {
		return hypotheses.size();
	}

	@Override
	public DestinationHypothesis get(int index) {
		return hypotheses.get(index);
	}

	@Override
	public Iterator<DestinationHypothesis> iterator() {
		return hypotheses.iterator();
	}

	@Override
	public DestinationHypothesis[] toArray() {
		DestinationHypothesis[] array = new DestinationHypothesis[hypotheses
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

	public void add(DestinationHypothesis hypothesis) {
		hypotheses.add(hypothesis);
	}

	public Items<Position> best() {

		// Best weight.
		double best = 0;
		for (DestinationHypothesis hypothesis : hypotheses) {
			double weight = hypothesis.weight();
			if (weight > best) {
				best = weight;
			}
		}

		// Select best hypotheses.
		ItemsList<Position> result = new ItemsList<>();
		for (DestinationHypothesis hypothesis : hypotheses) {
			if (hypothesis.weight() == best) {
				result.add(hypothesis.position());
			}
		}
		return result;
	}

	public void normalize() {

		// Sum.
		double sum = 0;
		int size = hypotheses.size();
		for (int i = 0; i < size; i++) {
			sum += hypotheses.get(i).weight();
		}

		// Normalize.
		for (int i = 0; i < size; i++) {
			DestinationHypothesis hypothesis = hypotheses.get(i);
			hypothesis.weight(hypothesis.weight() / sum);
		}
	}
}