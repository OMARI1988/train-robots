/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.collections;

import java.util.Iterator;

import com.trainrobots.NotImplementedException;

public class SingleItemIterator<T> implements Iterator<T> {

	private final T item;
	private boolean hasNext = true;

	public SingleItemIterator(T item) {
		this.item = item;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public T next() {
		hasNext = false;
		return item;
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}
}