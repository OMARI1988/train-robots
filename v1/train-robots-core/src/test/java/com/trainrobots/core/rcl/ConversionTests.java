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

import com.trainrobots.core.nodes.Node;

public class ConversionTests {

	@Test
	public void shouldConvertActionAttribute() {
		testConversion(new ActionAttribute(Action.take), "(action: take)");
	}

	@Test
	public void shouldConvertActionAttributeWithAlignment() {
		testConversion(new ActionAttribute(Action.take, 1, 2),
				"(action: take (token: 1 2))");
	}

	@Test
	public void shouldConvertColorAttribute() {
		testConversion(new ColorAttribute(Color.cyan), "(color: cyan)");
	}

	@Test
	public void shouldConvertColorAttributeWithAlignment() {
		testConversion(new ColorAttribute(Color.cyan, 7, 7),
				"(color: cyan (token: 7))");
	}

	@Test
	public void shouldConvertTypeAttribute() {
		testConversion(new TypeAttribute(Type.stack), "(type: stack)");
	}

	@Test
	public void shouldConvertTypeAttributeWithAlignment() {
		testConversion(new TypeAttribute(Type.stack, 4, 6),
				"(type: stack (token: 4 6))");
	}

	@Test
	public void shouldConvertIndicatorAttribute() {
		testConversion(new IndicatorAttribute(Indicator.left),
				"(indicator: left)");
	}

	@Test
	public void shouldConvertIndicatorAttributeWithAlignment() {
		testConversion(new IndicatorAttribute(Indicator.left, 12, 12),
				"(indicator: left (token: 12))");
	}

	@Test
	public void shouldConvertEntity() {
		testConversion(new Entity(new ColorAttribute(Color.green),
				new TypeAttribute(Type.prism)),
				"(entity: (color: green) (type: prism))");
	}

	@Test
	public void shouldConvertSpatialRelation() {
		testConversion(new SpatialRelation(
				new RelationAttribute(Relation.above), new Entity(
						new ColorAttribute(Color.red), new TypeAttribute(
								Type.cube))),
				"(spatial-relation: (relation: above) (entity: (color: red) (type: cube)))");
	}

	@Test
	public void shouldConvertEvent() {
		testConversion(
				new Event(new ActionAttribute(Action.move), new Entity(
						new ColorAttribute(Color.green), new TypeAttribute(
								Type.prism)), new SpatialRelation(
						new RelationAttribute(Relation.above), new Entity(
								new ColorAttribute(Color.red),
								new TypeAttribute(Type.cube)))),
				"(event: (action: move) (entity: (color: green) (type: prism)) (destination: (spatial-relation: (relation: above) (entity: (color: red) (type: cube)))))");
	}

	@Test
	public void shouldConvertSequence() {
		testConversion(
				new Sequence(
						new Event(new ActionAttribute(Action.take), new Entity(
								1, new IndicatorAttribute(Indicator.top),
								new TypeAttribute(Type.cube),
								new SpatialRelation(new RelationAttribute(
										Relation.part), new Entity(
										new ColorAttribute(Color.blue),
										new TypeAttribute(Type.stack))))),
						new Event(
								new ActionAttribute(Action.drop),
								new Entity(new TypeAttribute(Type.reference), 1),
								new SpatialRelation(Entity.cardinal(
										new CardinalAttribute(2),
										new TypeAttribute(Type.tile)),
										new RelationAttribute(Relation.forward)))),
				"(sequence: (event: (action: take) (entity: (id: 1) (indicator: top) (type: cube) (spatial-relation: (relation: part) (entity: (color: blue) (type: stack))))) (event: (action: drop) (entity: (type: reference) (reference-id: 1)) (destination: (spatial-relation: (measure: (entity: (cardinal: 2) (type: tile))) (relation: forward)))))");
	}

	@Test
	public void shouldConvertTypeReference() {
		testConversion(new Entity(new ColorAttribute(Color.green),
				new TypeAttribute(Type.typeReference)),
				"(entity: (color: green) (type: type-reference))");
	}

	private static void testConversion(Rcl expectedRcl, String expectedText) {

		// Format.
		Node expectedNode = Node.fromString(expectedText);
		assertEquals(expectedNode, expectedRcl.toNode());

		// Parse.
		Rcl actualRcl = Rcl.fromNode(expectedNode);
		assertEquals(expectedNode, actualRcl.toNode());
	}
}