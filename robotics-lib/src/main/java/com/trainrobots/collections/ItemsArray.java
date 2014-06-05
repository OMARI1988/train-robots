/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.collections;

import java.util.Iterator;

public class ItemsArray<T> implements Items<T> {

	private final T[] items;

	public ItemsArray(T... items) {
		this.items = items;
	}

	@Override
	public int count() {
		return items.length;
	}

	@Override
	public T get(int index) {
		return items[index];
	}

	@Override
	public T[] toArray() {
		return items.clone();
	}

	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator(items);
	}
}