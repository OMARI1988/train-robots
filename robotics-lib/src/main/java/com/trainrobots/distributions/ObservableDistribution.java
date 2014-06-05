/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.trainrobots.collections.Items;
import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Layout;

public abstract class ObservableDistribution extends Distribution implements
		Items<Observable> {

	private final List<Observable> observables = new ArrayList<Observable>();

	protected ObservableDistribution(Layout layout) {
		super(layout);
	}

	@Override
	public int count() {
		return observables.size();
	}

	@Override
	public Observable get(int index) {
		return observables.get(index);
	}

	@Override
	public Iterator<Observable> iterator() {
		return observables.iterator();
	}

	@Override
	public Observable[] toArray() {
		Observable[] array = new Observable[observables.size()];
		observables.toArray(array);
		return array;
	}

	protected void add(Observable observable) {
		observables.add(observable);
	}
}