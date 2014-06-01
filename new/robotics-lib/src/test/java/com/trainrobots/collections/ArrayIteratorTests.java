/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ArrayIteratorTests {

	@Test
	public void shouldIterateOverArray() {

		Integer[] items = { 32, 65, 78 };
		ArrayIterator<Integer> iterator = new ArrayIterator(items);

		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(items[0]));

		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(items[1]));

		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(items[2]));

		assertThat(iterator.hasNext(), is(false));
	}
}