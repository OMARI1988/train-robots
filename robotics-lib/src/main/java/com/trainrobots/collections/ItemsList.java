/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ItemsList<T> extends ArrayList<T> implements Items<T> {

	public ItemsList() {
	}

	@Override
	public int count() {
		return size();
	}

	@Override
	public T[] toArray() {
		Class itemType = get(0).getClass();
		T[] array = (T[]) Array.newInstance(itemType, size());
		toArray(array);
		return array;
	}
}