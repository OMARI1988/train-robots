/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.Iterator;

import com.trainrobots.collections.ArrayIterator;
import com.trainrobots.collections.Items;

public class Path implements Items<Losr> {

	private final Losr[] items;

	public Path(Losr... items) {
		this.items = items;
	}

	@Override
	public int count() {
		return items.length;
	}

	@Override
	public Losr get(int index) {
		return items[index];
	}

	@Override
	public Losr[] toArray() {
		return items.clone();
	}

	@Override
	public Iterator<Losr> iterator() {
		return new ArrayIterator(items);
	}

	public Path add(Losr item) {
		int size = items.length;
		Losr[] newItems = new Losr[size + 1];
		System.arraycopy(items, 0, newItems, 0, size);
		newItems[size] = item;
		return new Path(newItems);
	}
}