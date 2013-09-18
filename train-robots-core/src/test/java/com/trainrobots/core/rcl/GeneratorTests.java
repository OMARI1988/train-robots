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

public class GeneratorTests {

	@Test
	public void shouldGenerateEntity1() {
		Entity entity = new Entity(Type.board);
		assertEquals(entity.generate(), "the board");
	}

	@Test
	public void shouldGenerateEntity2() {
		Entity entity = new Entity(Color.red, Type.cube);
		assertEquals(entity.generate(), "the red cube");
	}

	@Test
	public void shouldGenerateEvent1() {
		Event event = new Event(Action.move,
				new Entity(Color.green, Type.prism), new SpatialRelation(
						SpatialIndicator.above,
						new Entity(Color.red, Type.cube)));
		assertEquals(event.generate(),
				"move the green prism above the red cube");
	}

	@Test
	public void shouldGenerateEvent2() {
		Event event = new Event(Action.move,
				new Entity(Color.green, Type.prism), new SpatialRelation(
						SpatialIndicator.within, new Entity(
								SpatialIndicator.back, SpatialIndicator.left,
								Type.corner)));
		assertEquals(event.generate(),
				"move the green prism within the back left corner");
	}

	@Test
	public void shouldGenerateSequence() {
		Sequence sequence = new Sequence(new Event(Action.take, new Entity(1,
				SpatialIndicator.top, Type.cube, new SpatialRelation(
						SpatialIndicator.part, new Entity(Color.blue,
								Type.stack)))), new Event(Action.drop,
				new Entity(Type.reference, 1),
				new SpatialRelation(Entity.cardinal(2, Type.tile),
						SpatialIndicator.forward)));
		assertEquals(
				sequence.generate(),
				"take the top cube that is part of the blue stack and drop it two tiles forward");
	}

	@Test
	public void shouldGenerateTypeReference() {
		Sequence sequence = Sequence
				.fromString("(sequence: (event: (action: take) (entity: (id: 1) (color: gray) (type: cube))) (event: (action: drop) (entity: (type: reference) (reference-id: 1)) (destination: (spatial-relation: (spatial-indicator: above) (entity: (color: green) (type: type-reference) (reference-id: 1))))))");

		assertEquals(sequence.generate(),
				"take the gray cube and drop it above the green one");
	}

	@Test
	public void shouldGenerateRegion() {
		Entity entity = Entity
				.fromString("(entity: (spatial-indicator: right) (type: region))");
		assertEquals(entity.generate(), "the right of the board");
	}

}