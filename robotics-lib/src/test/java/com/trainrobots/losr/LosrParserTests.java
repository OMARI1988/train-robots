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

public class LosrParserTests {

	@Test
	public void shouldParseText() {
		assertThat(Losr.parse("(text: red)"), is(new Text("red")));
	}

	@Test
	public void shouldParseTextWithContext() {
		assertThat(Losr.parse("(text: red (token: 1 2))"), is(new Text(
				new TextContext(1, 2), "red")));
	}

	@Test
	public void shouldParseSymbol() {
		assertThat(Losr.parse("(symbol: .)"), is(new Symbol('.')));
	}

	@Test
	public void shouldParseSymbolWithContext() {
		assertThat(Losr.parse("(symbol: . (token: 18))"), is(new Symbol(
				new TextContext(18), '.')));
	}

	@Test
	public void shouldParseOrdinal() {
		assertThat(Losr.parse("(ordinal: 22)"), is(new Ordinal(22)));
	}

	@Test
	public void shouldParseOrdinalWithContext() {
		assertThat(Losr.parse("(ordinal: 22 (token: 5 6))"), is(new Ordinal(
				new TextContext(5, 6), 22)));
	}

	@Test
	public void shouldParseCardinal() {
		assertThat(Losr.parse("(cardinal: 45)"), is(new Cardinal(45)));
	}

	@Test
	public void shouldParseCardinalWithContext() {
		assertThat(Losr.parse("(cardinal: 45 (token: 12))"), is(new Cardinal(
				new TextContext(12), 45)));
	}

	@Test
	public void shouldParseColor() {
		assertThat(Losr.parse("(color: red)"), is(new Color(Colors.Red)));
	}

	@Test
	public void shouldParseColorWithContext() {
		assertThat(Losr.parse("(color: red (token: 6 8))"), is(new Color(
				new TextContext(6, 8), Colors.Red)));
	}

	@Test
	public void shouldParseType() {
		assertThat(Losr.parse("(type: cube)"), is(new Type(Types.Cube)));
	}

	@Test
	public void shouldParseTypeWithContext() {
		assertThat(Losr.parse("(type: cube (token: 1))"), is(new Type(
				new TextContext(1), Types.Cube)));
	}

	@Test
	public void shouldParseAction() {
		assertThat(Losr.parse("(action: move)"), is(new Action(Actions.Move)));
	}

	@Test
	public void shouldParseActionWithContext() {
		assertThat(Losr.parse("(action: move (token: 12 16))"), is(new Action(
				new TextContext(12, 16), Actions.Move)));
	}

	@Test
	public void shouldParseRelation() {
		assertThat(Losr.parse("(relation: above)"), is(new Relation(
				Relations.Above)));
	}

	@Test
	public void shouldParseRelationWithContext() {
		assertThat(Losr.parse("(relation: above (token: 4))"), is(new Relation(
				new TextContext(4), Relations.Above)));
	}

	@Test
	public void shouldParseIndicator() {
		assertThat(Losr.parse("(indicator: nearest)"), is(new Indicator(
				Indicators.Nearest)));
	}

	@Test
	public void shouldParseIndicatorWithContext() {
		assertThat(Losr.parse("(indicator: nearest (token: 5))"),
				is(new Indicator(new TextContext(5), Indicators.Nearest)));
	}

	@Test
	public void shouldParseEntity1() {
		assertThat(Losr.parse("(entity: (type: prism))"), is(new Entity(
				Types.Prism)));
	}

	@Test
	public void shouldParseEntity2() {
		assertThat(
				Losr.parse("(entity: (type: prism) (spatial-relation: (relation: above) (entity: (type: cube))))"),
				is(new Entity(Types.Prism, new SpatialRelation(Relations.Above,
						Types.Cube))));
	}

	@Test
	public void shouldParseEntityWithContext() {
		assertThat(Losr.parse("(entity: (type: prism (token: 5)))"),
				is(new Entity(new Type(new TextContext(5), Types.Prism))));
	}

	@Test
	public void shouldParseEntityWithId() {
		assertThat(Losr.parse("(entity: (id: 1) (type: prism))"),
				is(new Entity(1, Types.Prism)));
	}

	@Test
	public void shouldParseEvent1() {
		assertThat(
				Losr.parse("(event: (action: take) (entity: (type: cube)))"),
				is(new Event(Actions.Take, Types.Cube)));
	}

	@Test
	public void shouldParseEvent2() {
		assertThat(
				Losr.parse("(event: (action: move) (entity: (type: cube)) (destination: (spatial-relation: (relation: above) (entity: (type: prism)))))"),
				is(new Event(Actions.Move, Types.Cube, new Destination(
						new SpatialRelation(Relations.Above, Types.Prism)))));
	}

	@Test
	public void shouldParseEventWithContext() {
		assertThat(
				Losr.parse("(event: (action: take (token: 1 2)) (entity: (type: cube (token: 3 4))))"),
				is(new Event(new Action(new TextContext(1, 2), Actions.Take),
						new Entity(new Type(new TextContext(3, 4), Types.Cube)))));
	}

	@Test
	public void shouldParseSpatialRelation() {
		assertThat(
				Losr.parse("(spatial-relation: (relation: above) (entity: (type: cube)))"),
				is(new SpatialRelation(Relations.Above, Types.Cube)));
	}

	@Test
	public void shouldParseDestination1() {
		assertThat(
				Losr.parse("(destination: (spatial-relation: (relation: above) (entity: (type: cube))))"),
				is(new Destination(new SpatialRelation(Relations.Above,
						Types.Cube))));
	}

	@Test
	public void shouldParseDestination2() {
		assertThat(
				Losr.parse("(destination: (marker: destination) (spatial-relation: (relation: above) (entity: (type: cube))))"),
				is(new Destination(new Marker(), new SpatialRelation(
						Relations.Above, Types.Cube))));
	}

	@Test
	public void shouldParseSequence() {
		assertThat(
				Losr.parse("(sequence: (event: (action: take) (entity: (type: cube))) (event: (action: take) (entity: (type: prism))))"),
				is(new Sequence(new Event(Actions.Take, Types.Cube), new Event(
						Actions.Take, Types.Prism))));
	}

	@Test
	public void shouldParseMarker() {
		assertThat(Losr.parse("(marker)"), is(new Marker()));
	}

	@Test
	public void shouldParseMarker2() {
		assertThat(Losr.parse("(marker: (token: 5))"), is(new Marker(
				new TextContext(5))));
	}
}