/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TraversalTests {

	@Test
	public void shouldTraverseText() {
		test(1, new Text("red"));
	}

	@Test
	public void shouldTraverseSymbol() {
		test(1, new Symbol('.'));
	}

	@Test
	public void shouldTraverseOrdinal() {
		test(1, new Ordinal(22));
	}

	@Test
	public void shouldTraverseCardinal() {
		test(1, new Cardinal(45));
	}

	@Test
	public void shouldTraverseColor() {
		test(1, new Color(Colors.Red));
	}

	@Test
	public void shouldTraverseType() {
		test(1, new Type(Types.Cube));
	}

	@Test
	public void shouldTraverseAction() {
		test(1, new Action(Actions.Move));
	}

	@Test
	public void shouldTraverseRelation() {
		test(1, new Relation(Relations.Above));
	}

	@Test
	public void shouldTraverseEntity1() {
		test(2, new Entity(Types.Prism));
	}

	@Test
	public void shouldTraverseEntity2() {
		test(3, new Entity(Colors.Red, Types.Prism));
	}

	@Test
	public void shouldTraverseEvent() {
		test(4, new Event(Actions.Take, Types.Cube));
	}

	private static void test(int expectedCount, Losr losr) {
		assertThat(traverse(losr), is(expectedCount));
	}

	private static int traverse(Losr losr) {

		// Bounds.
		int size = losr.count();
		Exception before = null;
		Exception after = null;
		try {
			losr.get(-1);
		} catch (Exception exception) {
			before = exception;
		}
		assertThat(before, instanceOf(IndexOutOfBoundsException.class));
		try {
			losr.get(size);
		} catch (Exception exception) {
			after = exception;
		}
		assertThat(after, instanceOf(IndexOutOfBoundsException.class));

		// Traverse.
		int count = 1;
		for (int i = 0; i < size; i++) {
			Losr child = losr.get(i);
			count += traverse(child);
		}
		return count;
	}
}