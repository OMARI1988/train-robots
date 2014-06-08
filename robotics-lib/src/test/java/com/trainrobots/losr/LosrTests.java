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

public class LosrTests {

	@Test
	public void shouldCalculateSpans() {

		Event event = (Event) TestContext.treebank().command(18977).losr();

		assertThat(event.span(), is(new TextContext(1, 12)));
		assertThat(event.actionAttribute().span(), is(new TextContext(1, 2)));
		assertThat(event.entity().span(), is(new TextContext(4, 12)));
	}
}