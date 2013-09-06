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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.trainrobots.nlp.parser.Stack;
import com.trainrobots.nlp.trees.Node;

public class StackTests {

	@Test
	public void shouldPopStack() {

		// Stack.
		Node a = new Node("(A X)");
		Node b = new Node("(B X)");
		Node c = new Node("(C X)");
		Stack stack = new Stack();
		stack.push(a);
		stack.push(b);
		stack.push(c);

		// Verify.
		assertFalse(stack.empty());
		assertEquals(stack.toString(), "(C X) (B X) (A X)");
		assertEquals(stack.get(0), c);
		assertEquals(stack.get(1), b);
		assertEquals(stack.get(2), a);
		assertNull(stack.get(3));

		// Pop.
		stack.pop();

		// Verify.
		assertFalse(stack.empty());
		assertEquals(stack.toString(), "(B X) (A X)");
		assertEquals(stack.get(0), b);
		assertEquals(stack.get(1), a);
		assertNull(stack.get(2));

		// Pop.
		stack.pop();

		// Verify.
		assertFalse(stack.empty());
		assertEquals(stack.toString(), "(A X)");
		assertEquals(stack.get(0), a);
		assertNull(stack.get(1));

		// Pop.
		stack.pop();

		// Verify.
		assertTrue(stack.empty());
		assertEquals(stack.toString(), "EMPTY");
		assertNull(stack.get(0));

		// Push.
		stack.push(c);
		stack.push(a);

		// Verify.
		assertFalse(stack.empty());
		assertEquals(stack.toString(), "(A X) (C X)");
		assertEquals(stack.get(0), a);
		assertEquals(stack.get(1), c);
		assertNull(stack.get(2));
	}
}