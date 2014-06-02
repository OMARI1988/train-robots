/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class MultiMapTests {

	@Test
	public void shouldAddToMultiMap() {

		// Empty.
		MultiMap<String, Integer> map = new MultiMap<String, Integer>();
		assertThat(map.get("A"), is(nullValue()));

		// Add.
		map.add("A", 3);
		assertThat(map.get("A"), is(Arrays.asList(3)));

		// Add.
		map.add("A", 7);
		assertThat(map.get("A"), is(Arrays.asList(3, 7)));
	}
}