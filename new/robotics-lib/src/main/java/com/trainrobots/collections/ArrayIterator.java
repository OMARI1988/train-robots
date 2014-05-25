/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.collections;

import java.util.Iterator;

import com.trainrobots.NotImplementedException;

public class ArrayIterator<T> implements Iterator<T> {

	private final T[] items;
	private int count;

	public ArrayIterator(T[] items) {
		this.items = items;
	}

	@Override
	public boolean hasNext() {
		return count < items.length;
	}

	@Override
	public T next() {
		return items[count++];
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}
}