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
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.nlp.tokenizer.Tokenizer;
import com.trainrobots.nlp.trees.Node;

public class Tagger {

	private static final List<Chunk> chunks = new ArrayList<Chunk>();

	static {

		add("left hand", "(Attribute left)");
		add("right hand", "(Attribute right)");
		add("far right", "(Attribute far) (Attribute right)");

		add("pick up", "(Command (Action pick-up))");

		add("to the left bottom corner",
				"(SpatialIndicator to (Object (Description definite) (Attribute left) (Attribute back) (Type corner)))");

		add("to the top left corner",
				"(SpatialIndicator to (Object (Description definite) (Attribute top) (Attribute left) (Type corner)))");

		add("the left bottom corner",
				"(Object (Description definite) (Attribute left) (Attribute back) (Type corner))");

		add("the top left corner",
				"(Object (Description definite) (Attribute top) (Attribute left) (Type corner))");

		add("right in", "(SpatialIndicator in)");

		add("on top of", "(SpatialIndicator above)");
		add("the top of", "(SpatialIndicator above)");
		add("on to", "(SpatialIndicator above)");

		add("the nearest", "(Description definite) (Attribute nearest)");
		add("the farthest", "(Description definite) (Attribute furthest)");
		add("the furthest", "(Description definite) (Attribute furthest)");

		add("in front of", "(SpatialIndicator front)");

		add("to right of", "(SpatialIndicator right)");
		add("to left of", "(SpatialIndicator left)");
		add("to the right of", "(SpatialIndicator right)");
		add("to the left of", "(SpatialIndicator left)");
		add("to the position of", "(SpatialIndicator at)");
		add("to the position", "(SpatialIndicator at)");
		add("to your right", "(SpatialIndicator right)");
		add("to your left", "(SpatialIndicator left)");
		add("to the right", "(SpatialIndicator right)");
		add("to the left", "(SpatialIndicator left)");
		add("to right", "(SpatialIndicator right)");
		add("to left", "(SpatialIndicator left)");

		add("on right of", "(SpatialIndicator right)");
		add("on left of", "(SpatialIndicator left)");
		add("on the right of", "(SpatialIndicator right)");
		add("on the left of", "(SpatialIndicator left)");
		add("on the position of", "(SpatialIndicator at)");
		add("on the position", "(SpatialIndicator at)");
		add("on your right", "(SpatialIndicator right)");
		add("on your left", "(SpatialIndicator left)");
		add("on the right", "(SpatialIndicator right)");
		add("on the left", "(SpatialIndicator left)");
		add("on right", "(SpatialIndicator right)");
		add("on left", "(SpatialIndicator left)");

		add("close to", "(SpatialIndicator near)");
		add("closest to", "(SpatialIndicator nearest)");
		add("near to", "(SpatialIndicator near)");
		add("nearest to", "(SpatialIndicator nearest)");
		add("next to", "(SpatialIndicator adjacent)");
		add("furthest from", "(SpatialIndicator furthest)");
		add("furthest away", "(SpatialIndicator furthest)");
		add("furthest away from", "(SpatialIndicator furthest)");
		add("farthest from", "(SpatialIndicator furthest)");
		add("farthest away", "(SpatialIndicator furthest)");
		add("farthest away from", "(SpatialIndicator furthest)");
		add("that is", "(Link that-is)");
		add("which is", "(Link that-is)");
		add("in between", "(SpatialIndicator between)");
		add("light blue", "(Attribute cyan)");
		add("sky blue", "(Attribute cyan)");
		add("dark blue", "(Attribute blue)");
		add("deep blue", "(Attribute blue)");
		add("light gray", "(Attribute white)");
		add("light grey", "(Attribute white)");
	}

	private Tagger() {
	}

	public static List<Node> getSentences(String text) {

		// Tokenize.
		List<Node> sentences = tokenize(text);

		// Tag.
		for (Node sentence : sentences) {
			tag(sentence);
		}

		// Result.
		return sentences;
	}

	private static List<Node> tokenize(String text) {

		ArrayList<Node> sentences = new ArrayList<Node>();
		Node sentence = null;

		for (Node token : Tokenizer.getTokens(text).children) {
			if (token.tag.equals("End")) {
				sentence = null;
			} else {
				if (sentence == null) {
					sentence = new Node("Sentence");
					sentences.add(sentence);
				}
				sentence.add(token);
			}
		}

		return sentences;
	}

	private static void tag(Node sentence) {

		// Chunks.
		List<Node> input = sentence.children;
		for (int i = 0; i < input.size(); i++) {
			Chunk chunk = match(input, i);
			if (chunk != null) {
				for (int j = 0; j < chunk.tokens.length; j++) {
					input.remove(i);
				}
				for (int j = 0; j < chunk.nodes.size(); j++) {
					input.add(i + j, chunk.nodes.get(j).clone());
				}
			}
		}

		// Lexicon.
		for (int i = 0; i < input.size(); i++) {

			// Match text.
			Node node = input.get(i);
			if (!node.tag.equals("Text")) {
				continue;
			}

			// Lexicon.
			String value = node.getValue();
			Node match = Lexicon.get(value);
			if (match == null) {
				node.tag = "X";
				continue;
			}

			// Apply.
			input.set(i, match);
		}
	}

	private static Chunk match(List<Node> input, int index) {
		for (Chunk chunk : chunks) {
			if (match(input, index, chunk)) {
				return chunk;
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

	private static void add(String text, String entry) {
		Chunk chunk = new Chunk();
		chunk.tokens = text.split(" ");
		if (chunk.tokens.length == 1) {
			throw new CoreException(
					"Single token chunk should be moved to the lexicon.");
		}
		chunk.nodes = Node.listFromString(entry);
		chunks.add(chunk);
	}
}