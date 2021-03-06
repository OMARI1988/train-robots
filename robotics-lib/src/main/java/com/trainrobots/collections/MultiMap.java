/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MultiMap<K, V> implements Iterable<Map.Entry<K, Items<V>>> {

	private final Map<K, ItemsList<V>> map = new HashMap<>();

	public void add(K key, V value) {
		ItemsList<V> values = map.get(key);
		if (values == null) {
			map.put(key, values = new ItemsList<V>());
		}
		values.add(value);
	}

	public Items<V> get(K key) {
		return map.get(key);
	}

	@Override
	public Iterator<Entry<K, Items<V>>> iterator() {
		return (Iterator) map.entrySet().iterator();
	}
}