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

package com.trainrobots.nlp.parsing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.parser.PartialList;

public class PartialListTests {

	@Test
	public void shouldAttachNodes() {

		// List.
		Node a = new Node("Action", "pick-up");
		Node b = new Node("Description", "definite");
		Node c = new Node("Color", "blue");
		Node d = new Node("Object", "prism");
		PartialList list = new PartialList(a, b, c, d);

		// Verify.
		assertEquals(list.size(), 4);
		assertEquals(list.toString(),
				"(Action pick-up) (Description definite) (Color blue) (Object prism)");
		assertEquals(list.get(1), a);
		assertEquals(list.get(2), b);
		assertEquals(list.get(3), c);
		assertEquals(list.get(4), d);

		// Right.
		list.right(3);
		assertEquals(list.size(), 3);
		assertEquals(list.get(1), a);
		assertEquals(list.get(2), b);
		assertEquals(list.get(3),
				Node.fromString("(Object (Color blue) prism)"));

		// Right.
		list.right(2);
		assertEquals(list.size(), 2);
		assertEquals(list.get(1), a);
		assertEquals(
				list.get(2),
				Node.fromString("(Object (Description definite) (Color blue) prism)"));

		// Left.
		list.left(1);
		assertEquals(list.size(), 1);
		assertEquals(
				list.get(1),
				Node.fromString("(Action pick-up (Object (Description definite) (Color blue) prism))"));
	}
}