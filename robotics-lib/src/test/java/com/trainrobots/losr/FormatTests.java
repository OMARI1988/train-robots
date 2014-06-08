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
		assertThat(new Text(new TextContext(1, 2), "red").toString(),
				is("(text: red (token: 1 2))"));
	}

	@Test
	public void shouldFormatSymbol() {
		assertThat(new Symbol('.').toString(), is("(symbol: .)"));
	}

	@Test
	public void shouldFormatSymbolWithContext() {
		assertThat(new Symbol(new TextContext(18), '.').toString(),
				is("(symbol: . (token: 18))"));
	}

	@Test
	public void shouldFormatOrdinal() {
		assertThat(new Ordinal(22).toString(), is("(ordinal: 22)"));
	}

	@Test
	public void shouldFormatOrdinalWithContext() {
		assertThat(new Ordinal(new TextContext(5, 6), 22).toString(),
				is("(ordinal: 22 (token: 5 6))"));
	}

	@Test
	public void shouldFormatCardinal() {
		assertThat(new Cardinal(45).toString(), is("(cardinal: 45)"));
	}

	@Test
	public void shouldCardinalWithContext() {
		assertThat(new Cardinal(new TextContext(12), 45).toString(),
				is("(cardinal: 45 (token: 12))"));
	}

	@Test
	public void shouldFormatColor() {
		assertThat(new Color(Colors.Red).toString(), is("(color: red)"));
	}

	@Test
	public void shouldFormatColorWithContext() {
		assertThat(new Color(new TextContext(6, 8), Colors.Red).toString(),
				is("(color: red (token: 6 8))"));
	}

	@Test
	public void shouldFormatType() {
		assertThat(new Type(Types.Cube).toString(), is("(type: cube)"));
	}

	@Test
	public void shouldFormatTypeWithContext() {
		assertThat(new Type(new TextContext(1), Types.Cube).toString(),
				is("(type: cube (token: 1))"));
	}

	@Test
	public void shouldFormatAction() {
		assertThat(new Action(Actions.Move).toString(), is("(action: move)"));
	}

	@Test
	public void shouldFormatActionWithContext() {
		assertThat(
				new Action(new TextContext(12, 16), Actions.Move).toString(),
				is("(action: move (token: 12 16))"));
	}

	@Test
	public void shouldFormatRelation() {
		assertThat(new Relation(Relations.Above).toString(),
				is("(relation: above)"));
	}

	@Test
	public void shouldFormatRelationWithContext() {
		assertThat(
				new Relation(new TextContext(4), Relations.Above).toString(),
				is("(relation: above (token: 4))"));
	}

	@Test
	public void shouldFormatIndicator() {
		assertThat(new Indicator(Indicators.Nearest).toString(),
				is("(indicator: nearest)"));
	}

	@Test
	public void shouldFormatIndicatorWithContext() {
		assertThat(
				new Indicator(new TextContext(5), Indicators.Nearest)
						.toString(),
				is("(indicator: nearest (token: 5))"));
	}

	@Test
	public void shouldFormatEntity1() {
		assertThat(new Entity(Types.Prism).toString(),
				is("(entity: (type: prism))"));
	}

	@Test
	public void shouldFormatEntity2() {
		assertThat(
				new Entity(Types.Prism, new SpatialRelation(Relations.Above,
						Types.Cube)).toString(),
				is("(entity: (type: prism) (spatial-relation: (relation: above) (entity: (type: cube))))"));
	}

	@Test
	public void shouldFormatEntityWithContext() {
		assertThat(
				new Entity(new Type(new TextContext(5), Types.Prism))
						.toString(),
				is("(entity: (type: prism (token: 5)))"));
	}

	@Test
	public void shouldFormatEntityWithId() {
		assertThat(new Entity(1, Types.Prism).toString(),
				is("(entity: (id: 1) (type: prism))"));
	}

	@Test
	public void shouldFormatEvent1() {
		assertThat(new Event(Actions.Take, Types.Cube).toString(),
				is("(event: (action: take) (entity: (type: cube)))"));
	}

	@Test
	public void shouldFormatEvent2() {
		assertThat(
				new Event(Actions.Move, Types.Cube, new Destination(
						new SpatialRelation(Relations.Above, Types.Prism))).toString(),
				is("(event: (action: move) (entity: (type: cube)) (destination: (spatial-relation: (relation: above) (entity: (type: prism)))))"));
	}

	@Test
	public void shouldFormatEventWithContext() {
		assertThat(
				new Event(
						new Action(new TextContext(1, 2), Actions.Take),
						new Entity(new Type(new TextContext(3, 4), Types.Cube)))
						.toString(),
				is("(event: (action: take (token: 1 2)) (entity: (type: cube (token: 3 4))))"));
	}

	@Test
	public void shouldFormatSpatialRelation() {
		assertThat(
				new SpatialRelation(Relations.Above, Types.Cube).toString(),
				is("(spatial-relation: (relation: above) (entity: (type: cube)))"));
	}

	@Test
	public void shouldFormatDestination() {
		assertThat(
				new Destination(
						new SpatialRelation(Relations.Above, Types.Cube)).toString(),
				is("(destination: (spatial-relation: (relation: above) (entity: (type: cube))))"));
	}

	@Test
	public void shouldFormatSequence() {
		assertThat(
				new Sequence(new Event(Actions.Take, Types.Cube), new Event(
						Actions.Take, Types.Prism)).toString(),
				is("(sequence: (event: (action: take) (entity: (type: cube))) (event: (action: take) (entity: (type: prism))))"));
	}
}