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

public class SingleItemIteratorTests {

	@Test
	public void shouldIterateOverSingleItem() {

		SingleItemIterator<Integer> iterator = new SingleItemIterator(32);
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(32));
		assertThat(iterator.hasNext(), is(false));
	}
}