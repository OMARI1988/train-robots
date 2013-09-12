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
	public void shouldConvertEntity() {
		testConversion(new Entity(Color.green, Type.prism),
				"(entity: (color: green) (type: prism))");
	}

	@Test
	public void shouldConvertSpatialRelation() {
		testConversion(
				new SpatialRelation(SpatialIndicator.above, new Entity(
						Color.red, Type.cube)),
				"(spatial-relation: (spatial-indicator: above) (entity: (color: red) (type: cube)))");
	}

	@Test
	public void shouldConvertEvent() {
		testConversion(
				new Event(Action.move, new Entity(Color.green, Type.prism),
						new SpatialRelation(SpatialIndicator.above, new Entity(
								Color.red, Type.cube))),
				"(event: (action: move) (entity: (color: green) (type: prism)) (destination: (spatial-relation: (spatial-indicator: above) (entity: (color: red) (type: cube)))))");
	}

	private static void testConversion(Rcl expectedRcl, String expectedText) {

		// Format.
		Node expectedNode = Node.fromString(expectedText);
		assertEquals(expectedRcl.toNode(), expectedNode);

		// Parse.
		Rcl actualRcl = Rcl.fromNode(expectedNode);
		assertEquals(actualRcl.toNode(), expectedNode);
	}
}