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

	@Test
	public void shouldFormatColor() {
		assertThat(new Color(Colors.Red).toString(), is("(color: red)"));
	}

	@Test
	public void shouldFormatType() {
		assertThat(new Type(Types.Cube).toString(), is("(type: cube)"));
	}

	@Test
	public void shouldFormatAction() {
		assertThat(new Action(Actions.Move).toString(), is("(action: move)"));
	}

	@Test
	public void shouldFormatEntity() {
		assertThat(new Entity(Types.Prism).toString(),
				is("(entity: (type: prism))"));
	}

	@Test
	public void shouldFormatEvent() {
		assertThat(new Event(Actions.Take, Types.Cube).toString(),
				is("(event: (action: take) (entity: (type: cube)))"));
	}
}