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

public class LosrReaderTests {

	@Test
	public void shouldReadText() {
		assertThat(Losr.read("(text: red)"), is(new Text("red")));
	}

	@Test
	public void shouldReadTextWithContext() {
		assertThat(Losr.read("(text: red (token: 1 2))"), is(new Text(
				new TokenContext(1, 2), "red")));
	}

	@Test
	public void shouldReadSymbol() {
		assertThat(Losr.read("(symbol: .)"), is(new Symbol('.')));
	}

	@Test
	public void shouldReadSymbolWithContext() {
		assertThat(Losr.read("(symbol: . (token: 18))"), is(new Symbol(
				new TokenContext(18), '.')));
	}

	@Test
	public void shouldReadOrdinal() {
		assertThat(Losr.read("(ordinal: 22)"), is(new Ordinal(22)));
	}

	@Test
	public void shouldReadOrdinalWithContext() {
		assertThat(Losr.read("(ordinal: 22 (token: 5 6))"), is(new Ordinal(
				new TokenContext(5, 6), 22)));
	}

	@Test
	public void shouldReadCardinal() {
		assertThat(Losr.read("(cardinal: 45)"), is(new Cardinal(45)));
	}

	@Test
	public void shouldReadCardinalWithContext() {
		assertThat(Losr.read("(cardinal: 45 (token: 12))"), is(new Cardinal(
				new TokenContext(12), 45)));
	}

	@Test
	public void shouldReadColor() {
		assertThat(Losr.read("(color: red)"), is(new Color(Colors.Red)));
	}

	@Test
	public void shouldReadColorWithContext() {
		assertThat(Losr.read("(color: red (token: 6 8))"), is(new Color(
				new TokenContext(6, 8), Colors.Red)));
	}

	@Test
	public void shouldReadType() {
		assertThat(Losr.read("(type: cube)"), is(new Type(Types.Cube)));
	}

	@Test
	public void shouldReadTypeWithContext() {
		assertThat(Losr.read("(type: cube (token: 1))"), is(new Type(
				new TokenContext(1), Types.Cube)));
	}

	@Test
	public void shouldReadAction() {
		assertThat(Losr.read("(action: move)"), is(new Action(Actions.Move)));
	}

	@Test
	public void shouldReadActionWithContext() {
		assertThat(Losr.read("(action: move (token: 12 16))"), is(new Action(
				new TokenContext(12, 16), Actions.Move)));
	}

	@Test
	public void shouldReadEntity() {
		assertThat(Losr.read("(entity: (type: prism))"), is(new Entity(
				Types.Prism)));
	}

	@Test
	public void shouldReadEntityWithContext() {
		assertThat(Losr.read("(entity: (type: prism (token: 5)))"),
				is(new Entity(new Type(new TokenContext(5), Types.Prism))));
	}

	@Test
	public void shouldReadEvent() {
		assertThat(Losr.read("(event: (action: take) (entity: (type: cube)))"),
				is(new Event(Actions.Take, Types.Cube)));
	}

	@Test
	public void shouldReadEventWithContext() {
		assertThat(
				Losr.read("(event: (action: take (token: 1 2)) (entity: (type: cube (token: 3 4))))"),
				is(new Event(
						new Action(new TokenContext(1, 2), Actions.Take),
						new Entity(new Type(new TokenContext(3, 4), Types.Cube)))));
	}
}