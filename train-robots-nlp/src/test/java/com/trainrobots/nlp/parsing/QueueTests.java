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

import com.trainrobots.nlp.parser.Queue;
import com.trainrobots.nlp.trees.Node;

public class QueueTests {

	@Test
	public void shouldFormatQueue() {
		Queue queue = new Queue(new Node("(A B)"), new Node("(C D)"));
		assertEquals(queue.toString(), "(A B) (C D)");
	}

	@Test
	public void shouldReadQueue() {

		// Queue.
		Node a = new Node("(A X)");
		Node b = new Node("(B X)");
		Node c = new Node("(C X)");
		Queue queue = new Queue(a, b, c);

		// Verify.
		assertFalse(queue.empty());
		assertEquals(queue.toString(), "(A X) (B X) (C X)");
		assertEquals(queue.get(0), a);
		assertEquals(queue.get(1), b);
		assertEquals(queue.get(2), c);
		assertNull(queue.get(3));

		// Read.
		queue.read();

		// Verify.
		assertFalse(queue.empty());
		assertEquals(queue.toString(), "(B X) (C X)");
		assertEquals(queue.get(0), b);
		assertEquals(queue.get(1), c);
		assertNull(queue.get(2));
		assertNull(queue.get(3));

		// Read.
		queue.read();
		queue.read();

		// Verify.
		assertTrue(queue.empty());
		assertEquals(queue.toString(), "EMPTY");
		assertNull(queue.get(0));
		assertNull(queue.get(1));
		assertNull(queue.get(2));
		assertNull(queue.get(3));
	}
}