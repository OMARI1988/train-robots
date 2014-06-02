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

public class FormatTests {

	@Test
	public void shouldFormatText() {
		assertThat(new Text("red").toString(), is("(text: red)"));
	}

	@Test
	public void shouldFormatSymbol() {
		assertThat(new Symbol(".").toString(), is("(symbol: .)"));
	}

	@Test
	public void shouldFormatOrdinal() {
		assertThat(new Ordinal("22nd", 22).toString(), is("(ordinal: 22nd)"));
	}

	@Test
	public void shouldFormatCardinal() {
		assertThat(new Cardinal("45", 45).toString(), is("(cardinal: 45)"));
	}
}