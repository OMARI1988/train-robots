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
	public void shouldReadSymbol() {
		assertThat(Losr.read("(symbol: .)"), is(new Symbol(".")));
	}

	@Test
	public void shouldReadOrdinal() {
		assertThat(Losr.read("(ordinal: 22nd)"), is(new Ordinal("22nd")));
	}

	@Test
	public void shouldReadCardinal() {
		assertThat(Losr.read("(cardinal: 45)"), is(new Cardinal("45")));
	}

	@Test
	public void shouldReadColor() {
		assertThat(Losr.read("(color: red)"), is(new Color(Colors.Red)));
	}

	@Test
	public void shouldReadType() {
		assertThat(Losr.read("(type: cube)"), is(new Type(Types.Cube)));
	}

	@Test
	public void shouldReadAction() {
		assertThat(Losr.read("(action: move)"), is(new Action(Actions.Move)));
	}

	@Test
	public void shouldReadEntity() {
		assertThat(Losr.read("(entity: (type: prism))"), is(new Entity(
				Types.Prism)));
	}

	@Test
	public void shouldReadEvent() {
		assertThat(Losr.read("(event: (action: take) (entity: (type: cube)))"),
				is(new Event(Actions.Take, Types.Cube)));
	}
}