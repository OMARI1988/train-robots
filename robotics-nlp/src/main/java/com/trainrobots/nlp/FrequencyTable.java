/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FrequencyTable implements Iterable<FrequencyTable.Entry> {

	private Map<String, Entry> entries = new HashMap<>();

	public void add(String key) {
		Entry entry = entries.get(key);
		if (entry == null) {
			entries.put(key, entry = new Entry(key));
		}
		entry.count++;
	}

	@Override
	public Iterator<Entry> iterator() {
		ArrayList<Entry> list = new ArrayList<Entry>(entries.values());
		list.sort((a, b) -> {
			return -Integer.compare(a.count, b.count);
		});
		return list.iterator();
	}

	public static class Entry {

		private final String key;
		int count;

		public Entry(String key) {
			this.key = key;
		}

		public String key() {
			return key;
		}

		public int count() {
			return count;
		}

		@Override
		public String toString() {
			return key + ": " + count;
		}
	}
}