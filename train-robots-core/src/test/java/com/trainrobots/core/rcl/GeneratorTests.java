/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.core.rcl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.trainrobots.core.corpus.Corpus;

public class GeneratorTests {

	@Test
	public void shouldGenerateEntity1() {
		Entity entity = new Entity(new TypeAttribute(Type.board));
		assertEquals(entity.generate(), "The board.");
	}

	@Test
	public void shouldGenerateEntity2() {
		Entity entity = new Entity(new ColorAttribute(Color.red),
				new TypeAttribute(Type.cube));
		assertEquals(entity.generate(), "The red cube.");
	}

	@Test
	public void shouldGenerateEvent1() {
		Event event = new Event(new ActionAttribute(Action.move),
				new Entity(new ColorAttribute(Color.green), new TypeAttribute(
						Type.prism)), new SpatialRelation(
						new IndicatorAttribute(SpatialIndicator.above),
						new Entity(new ColorAttribute(Color.red),
								new TypeAttribute(Type.cube))));
		assertEquals(event.generate(),
				"Move the green prism above the red cube.");
	}

	@Test
	public void shouldGenerateEvent2() {
		Event event = new Event(new ActionAttribute(Action.move),
				new Entity(new ColorAttribute(Color.green), new TypeAttribute(
						Type.prism)), new SpatialRelation(
						new IndicatorAttribute(SpatialIndicator.within),
						new Entity(
								new IndicatorAttribute(SpatialIndicator.back),
								new IndicatorAttribute(SpatialIndicator.left),
								new TypeAttribute(Type.corner))));
		assertEquals(event.generate(),
				"Move the green prism within the back left corner.");
	}

	@Test
	public void shouldGenerateSequence() {
		Sequence sequence = new Sequence(
				new Event(new ActionAttribute(Action.take), new Entity(1,
						new IndicatorAttribute(SpatialIndicator.top),
						new TypeAttribute(Type.cube), new SpatialRelation(
								new IndicatorAttribute(SpatialIndicator.part),
								new Entity(new ColorAttribute(Color.blue),
										new TypeAttribute(Type.stack))))),
				new Event(
						new ActionAttribute(Action.drop),
						new Entity(new TypeAttribute(Type.reference), 1),
						new SpatialRelation(
								Entity.cardinal(2, new TypeAttribute(Type.tile)),
								new IndicatorAttribute(SpatialIndicator.forward))));
		assertEquals(
				sequence.generate(),
				"Pick up the top cube that is part of the blue stack and drop it two squares forward.");
	}

	@Test
	public void shouldGenerateTypeReference() {
		Sequence sequence = Sequence
				.fromString("(sequence: (event: (action: take) (entity: (id: 1) (color: gray) (type: cube))) (event: (action: drop) (entity: (type: reference) (reference-id: 1)) (destination: (spatial-relation: (spatial-indicator: above) (entity: (color: green) (type: type-reference) (reference-id: 1))))))");

		assertEquals(sequence.generate(),
				"Pick up the gray cube and drop it above the green one.");
	}

	@Test
	public void shouldGenerateTypeReferenceGroup() {
		Rcl rcl = Corpus.getCommand(24480).rcl;
		assertEquals(rcl.generate(),
				"Pick up the yellow cube and drop it above the green ones.");
	}

	@Test
	public void shouldGenerateRegion() {
		Entity entity = Entity
				.fromString("(entity: (spatial-indicator: right) (type: region))");
		assertEquals(entity.generate(), "The right.");
	}

	@Test
	public void shouldGenerateAdjacentRelation() {
		Entity entity = Entity
				.fromString("(entity: (type: cube) (spatial-relation: (spatial-indicator: adjacent) (entity: (type: prism))))");
		assertEquals(entity.generate(), "The cube adjacent to the prism.");
	}

	@Test
	public void shouldGenerateLeftRelation() {
		Rcl rcl = Corpus.getCommand(7517).rcl;
		assertEquals(rcl.generate(), "Move the blue cube two squares left.");
	}

	@Test
	public void shouldGenerateColorList1() {
		Entity entity = Entity
				.fromString("(entity: (color: red) (color: green) (type: stack))");
		assertEquals(entity.generate(), "The red and green stack.");
	}

	@Test
	public void shouldGenerateColorList2() {
		Entity entity = Entity
				.fromString("(entity: (color: red) (color: green) (color: blue) (type: stack))");
		assertEquals(entity.generate(), "The red, green and blue stack.");
	}

	@Test
	public void shouldGenerateCubeGroup() {
		Rcl rcl = Corpus.getCommand(3548).rcl;
		assertEquals(rcl.generate(),
				"Move the yellow prism above the green and red cubes.");
	}

	@Test
	public void shouldGenerateMeasure() {
		Rcl rcl = Corpus.getCommand(21001).rcl;
		assertEquals(rcl.generate(),
				"Move the individual yellow prism three squares in front of the gray prism.");
	}
}