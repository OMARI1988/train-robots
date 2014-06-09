/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.TestContext;
import com.trainrobots.collections.Items;

public class LosrTests {

	@Test
	public void shouldCalculateSpans() {

		// Event.
		Event event = (Event) TestContext.treebank().command(18977).losr();

		// Spans.
		assertThat(event.span(), is(new TextContext(1, 12)));
		assertThat(event.actionAttribute().span(), is(new TextContext(1, 2)));
		assertThat(event.entity().span(), is(new TextContext(4, 12)));
	}

	@Test
	public void shouldGetPath1() {

		// Path.
		Event event = (Event) TestContext.treebank().command(18977).losr();
		Items<Losr> path = event.path(event);

		// Verify.
		assertThat(path.count(), is(1));
		assertThat(path.get(0), is(event));
	}

	@Test
	public void shouldGetPath2() {

		// Path.
		Event event = (Event) TestContext.treebank().command(18977).losr();
		Relation relation = (Relation) event.get(1).get(2).get(0);
		Items<Losr> path = event.path(relation);

		// Verify.
		assertThat(path.count(), is(4));
		assertThat(path.get(0), is(event));
		assertThat(path.get(1), is(event.get(1)));
		assertThat(path.get(2), is(event.get(1).get(2)));
		assertThat(path.get(3), is(relation));
	}
}