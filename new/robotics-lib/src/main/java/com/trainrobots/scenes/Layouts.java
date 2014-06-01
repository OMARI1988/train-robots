/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.trainrobots.collections.ArrayIterator;
import com.trainrobots.collections.Items;

public class Layouts implements Items<Layout> {

	private final Layout[] layouts;
	private final Map<Integer, Layout> map = new HashMap<>();

	public Layouts(Items<Layout> layouts) {
		this.layouts = layouts.toArray();
		for (Layout layout : layouts) {
			map.put(layout.id(), layout);
		}
	}

	@Override
	public int count() {
		return layouts.length;
	}

	@Override
	public Layout get(int index) {
		return layouts[index];
	}

	public Layout of(int id) {
		Layout layout = map.get(id);
		if (layout == null) {
			throw new IllegalArgumentException(String.format(
					"The layout ID '%d' is not recognized.", id));
		}
		return layout;
	}

	@Override
	public Layout[] toArray() {
		return layouts.clone();
	}

	@Override
	public Iterator<Layout> iterator() {
		return new ArrayIterator(layouts);
	}
}