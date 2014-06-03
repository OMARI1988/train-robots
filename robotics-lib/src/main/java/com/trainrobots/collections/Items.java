/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.collections;

public interface Items<T> extends Iterable<T> {

	int count();

	T get(int index);

	T[] toArray();

	public static <T> boolean equals(Items<T> left, Items<T> right) {
		if (left == null) {
			return right == null;
		}
		if (right == null) {
			return left == null;
		}
		int size = left.count();
		if (right.count() != size) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (!left.get(i).equals(right.get(i))) {
				return false;
			}
		}
		return true;
	}
}