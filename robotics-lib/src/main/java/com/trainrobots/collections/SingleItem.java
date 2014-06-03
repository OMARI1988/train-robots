/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.collections;

import java.lang.reflect.Array;
import java.util.Iterator;

public class SingleItem<T> implements Items<T> {

	private final T item;

	public SingleItem(T item) {
		this.item = item;
	}

	@Override
	public Iterator<T> iterator() {
		return new SingleItemIterator(item);
	}

	@Override
	public int count() {
		return 1;
	}

	@Override
	public T get(int index) {
		if (index != 0) {
			throw new IndexOutOfBoundsException();
		}
		return item;
	}

	@Override
	public T[] toArray() {
		Class itemType = get(0).getClass();
		T[] array = (T[]) Array.newInstance(itemType, 1);
		array[0] = item;
		return array;
	}
}