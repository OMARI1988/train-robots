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
	public void shouldFormatTextWithContext() {
		assertThat(new Text(new TokenContext(1, 2), "red").toString(),
				is("(text: red (token: 1 2))"));
	}

	@Test
	public void shouldFormatSymbol() {
		assertThat(new Symbol(".").toString(), is("(symbol: .)"));
	}

	@Test
	public void shouldFormatSymbolWithContext() {
		assertThat(new Symbol(new TokenContext(18), ".").toString(),
				is("(symbol: . (token: 18))"));
	}

	@Test
	public void shouldFormatOrdinal() {
		assertThat(new Ordinal("22nd", 22).toString(), is("(ordinal: 22nd)"));
	}

	@Test
	public void shouldFormatOrdinalWithContext() {
		assertThat(new Ordinal(new TokenContext(5, 6), "22nd", 22).toString(),
				is("(ordinal: 22nd (token: 5 6))"));
	}

	@Test
	public void shouldFormatCardinal() {
		assertThat(new Cardinal("45", 45).toString(), is("(cardinal: 45)"));
	}

	@Test
	public void shouldCardinalWithContext() {
		assertThat(new Cardinal(new TokenContext(12), "45", 45).toString(),
				is("(cardinal: 45 (token: 12))"));
	}

	@Test
	public void shouldFormatColor() {
		assertThat(new Color(Colors.Red).toString(), is("(color: red)"));
	}

	@Test
	public void shouldFormatColorWithContext() {
		assertThat(new Color(new TokenContext(6, 8), Colors.Red).toString(),
				is("(color: red (token: 6 8))"));
	}

	@Test
	public void shouldFormatType() {
		assertThat(new Type(Types.Cube).toString(), is("(type: cube)"));
	}

	@Test
	public void shouldFormatTypeWithContext() {
		assertThat(new Type(new TokenContext(1), Types.Cube).toString(),
				is("(type: cube (token: 1))"));
	}

	@Test
	public void shouldFormatAction() {
		assertThat(new Action(Actions.Move).toString(), is("(action: move)"));
	}

	@Test
	public void shouldFormatActionWithContext() {
		assertThat(
				new Action(new TokenContext(12, 16), Actions.Move).toString(),
				is("(action: move (token: 12 16))"));
	}

	@Test
	public void shouldFormatEntity() {
		assertThat(new Entity(Types.Prism).toString(),
				is("(entity: (type: prism))"));
	}

	@Test
	public void shouldFormatEntityWithContext() {
		assertThat(
				new Entity(new Type(new TokenContext(5), Types.Prism))
						.toString(),
				is("(entity: (type: prism (token: 5)))"));
	}

	@Test
	public void shouldFormatEvent() {
		assertThat(new Event(Actions.Take, Types.Cube).toString(),
				is("(event: (action: take) (entity: (type: cube)))"));
	}

	@Test
	public void shouldFormatEventWithContext() {
		assertThat(
				new Event(
						new Action(new TokenContext(1, 2), Actions.Take),
						new Entity(new Type(new TokenContext(3, 4), Types.Cube)))
						.toString(),
				is("(event: (action: take (token: 1 2)) (entity: (type: cube (token: 3 4))))"));
	}
}