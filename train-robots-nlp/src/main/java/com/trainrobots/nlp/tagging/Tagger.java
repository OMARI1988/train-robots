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

package com.trainrobots.nlp.tagging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.nlp.tokenizer.Tokenizer;
import com.trainrobots.nlp.trees.Node;

public class Tagger {

	private static final Map<String, List<Chunk>> chunks = new HashMap<String, List<Chunk>>();

	private Tagger() {
	}

	public static Node getTokens(String text) {

		// Tokenize.
		Node tokens = Tokenizer.getTokens(text);
		List<Node> input = tokens.children;

		// Chunks.
		for (int i = 0; i < input.size(); i++) {
			Chunk chunk = match(input, i);
			if (chunk != null) {
				for (int j = 0; j < chunk.tokens.length; j++) {
					input.remove(i);
				}
				input.add(i, chunk.node);
			}
		}

		// Lexicon.
		for (int i = 0; i < input.size(); i++) {
			Node node = input.get(i);
			if (node.tag.equals("Text")) {
				String value = node.getValue();
				Node entry = Lexicon.get(value);
				if (entry != null) {
					input.set(i, entry);
				} else {
					node.tag = "X";
				}
			}
		}

		// Result.
		return tokens;
	}

	private static Chunk match(List<Node> input, int index) {
		String key = input.get(index).getValue();
		List<Chunk> list = chunks.get(key);
		if (list != null) {
			for (Chunk chunk : list) {
				if (match(input, index, chunk)) {
					return chunk;
				}
			}
		}
		return null;
	}

	private static boolean match(List<Node> input, int index, Chunk chunk) {

		String[] tokens = chunk.tokens;
		int size = tokens.length;
		if (index + size > input.size()) {
			return false;
		}

		for (int i = 0; i < size; i++) {
			Node actual = input.get(index + i);
			String expected = tokens[i];
			if (!actual.getValue().equals(expected)) {
				return false;
			}
		}
		return true;
	}

	static {

		add("pick up", "(Action pick-up)");

		add("on top of", "(SpatialRelation above)");
		add("the top of", "(SpatialRelation above)");
		add("on to", "(SpatialRelation above)");

		add("to right of", "(SpatialRelation right)");
		add("to left of", "(SpatialRelation left)");
		add("to the right of", "(SpatialRelation right)");
		add("to the left of", "(SpatialRelation left)");
		add("to the position of", "(SpatialRelation at)");
		add("to the position", "(SpatialRelation at)");
		add("to your right", "(Direction right)");
		add("to your left", "(Direction left)");
		add("to the right", "(SpatialRelation right)");
		add("to the left", "(SpatialRelation left)");
		add("to right", "(SpatialRelation right)");
		add("to left", "(SpatialRelation left)");

		add("on right of", "(SpatialRelation right)");
		add("on left of", "(SpatialRelation left)");
		add("on the right of", "(SpatialRelation right)");
		add("on the left of", "(SpatialRelation left)");
		add("on the position of", "(SpatialRelation at)");
		add("on the position", "(SpatialRelation at)");
		add("on your right", "(Direction right)");
		add("on your left", "(Direction left)");
		add("on the right", "(SpatialRelation right)");
		add("on the left", "(SpatialRelation left)");
		add("on right", "(SpatialRelation right)");
		add("on left", "(SpatialRelation left)");

		add("close to", "(SpatialRelation near)");
		add("closest to", "(SpatialRelation nearest)");
		add("near to", "(SpatialRelation near)");
		add("nearest to", "(SpatialRelation nearest)");
		add("next to", "(SpatialRelation adjacent)");
		add("furthest from", "(SpatialRelation furthest)");
		add("furthest away", "(SpatialRelation furthest)");
		add("furthest away from", "(SpatialRelation furthest)");
		add("farthest from", "(SpatialRelation furthest)");
		add("farthest away", "(SpatialRelation furthest)");
		add("farthest away from", "(SpatialRelation furthest)");
		add("that is", "(Link that-is)");
		add("which is", "(Link that-is)");
		add("sitting", "(Link that-is)");
		add("placed", "(Link that-is)");
		add("located", "(Link that-is)");
		add("situated", "(Link that-is)");
		add("in between", "(SpatialRelation between)");
		add("light blue", "(Color cyan)");
		add("dark blue", "(Color blue)");
		add("light gray", "(Color white)");
		add("light grey", "(Color white)");
	}

	private static void add(String text, String entry) {

		Chunk chunk = new Chunk();
		chunk.tokens = text.split(" ");
		chunk.node = Node.fromString(entry);

		String key = chunk.tokens[0];
		List<Chunk> list = chunks.get(key);
		if (list == null) {
			list = new ArrayList<Chunk>();
			chunks.put(key, list);
		}
		list.add(chunk);
	}
}