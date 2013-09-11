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

package com.trainrobots.core.nodes;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.trainrobots.core.nodes.Node;

public class NodeTests {

	@Test
	public void shouldWriteNode1() {

		Node a = new Node("A");
		a.add("B").add("X");
		a.add("C").add("Y");

		assertEquals(a.toString(), "(A (B X) (C Y))");
	}

	@Test
	public void shouldWriteNode2() {
		Node a = new Node("B", "X");
		assertEquals(a.toString(), "(B X)");
	}

	@Test
	public void shouldWriteNode3() {
		Node a = Node.fromString("(A B C)");
		assertEquals(a.toString(), "(A B C)");
	}

	@Test
	public void shouldReadNode1() {
		String text = "(S (VP Stop) (NP me))";
		assertEquals(Node.fromString(text).toString(), text);
	}

	@Test
	public void shouldReadNode2() {
		String text = "(S (VP    Stop)     (NP me))";
		assertEquals(Node.fromString(text).toString(), "(S (VP Stop) (NP me))");
	}

	@Test
	public void shouldReadNode3() {
		String text = "    (S (VP    Stop)     (NP me))";
		assertEquals(Node.fromString(text).toString(), "(S (VP Stop) (NP me))");
	}

	@Test
	public void shouldReadNode4() {
		String text = "    (S (VP    Stop)     (NP me)    )";
		assertEquals(Node.fromString(text).toString(), "(S (VP Stop) (NP me))");
	}

	@Test
	public void shouldReadList() {
		List<Node> list = Node.listFromString("(VP Stop) (NP me)");
		assertEquals(list.size(), 2);
		assertEquals(list.get(0), new Node("VP", "Stop"));
		assertEquals(list.get(1), new Node("NP", "me"));
	}
}