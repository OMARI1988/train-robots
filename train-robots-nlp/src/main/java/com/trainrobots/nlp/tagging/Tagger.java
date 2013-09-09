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

		add("left hand", "(attribute: left)");
		add("right hand", "(attribute: right)");
		add("far right", "(attribute: far) (attribute: right)");

		add("pick up", "(event: (action: pick-up))");

		add("to the left bottom corner",
				"(spatial-indicator: to (entity: (attribute: left) (attribute: back) (type: corner)))");

		add("to the top left corner",
				"(spatial-indicator: to (entity: (attribute: top) (attribute: left) (type: corner)))");

		add("the left bottom corner",
				"(entity: (attribute: left) (attribute: back) (type: corner))");

		add("the top left corner",
				"(entity: (attribute: top) (attribute: left) (type: corner))");

		add("right in", "(spatial-indicator: in)");

		add("on top of", "(spatial-indicator: above)");
		add("the top of", "(spatial-indicator: above)");
		add("on to", "(spatial-indicator: above)");

		add("the nearest", "(attribute: nearest)");
		add("the farthest", "(attribute: furthest)");
		add("the furthest", "(attribute: furthest)");

		add("in front of", "(spatial-indicator: front)");

		add("to right of", "(spatial-indicator: right)");
		add("to left of", "(spatial-indicator: left)");
		add("to the right of", "(spatial-indicator: right)");
		add("to the left of", "(spatial-indicator: left)");
		add("to the position of", "(spatial-indicator: at)");
		add("to the position", "(spatial-indicator: at)");
		add("to your right", "(spatial-indicator: right)");
		add("to your left", "(spatial-indicator: left)");
		add("to the right", "(spatial-indicator: right)");
		add("to the left", "(spatial-indicator: left)");
		add("to right", "(spatial-indicator: right)");
		add("to left", "(spatial-indicator: left)");

		add("on right of", "(spatial-indicator: right)");
		add("on left of", "(spatial-indicator: left)");
		add("on the right of", "(spatial-indicator: right)");
		add("on the left of", "(spatial-indicator: left)");
		add("on the position of", "(spatial-indicator: at)");
		add("on the position", "(spatial-indicator: at)");
		add("on your right", "(spatial-indicator: right)");
		add("on your left", "(spatial-indicator: left)");
		add("on the right", "(spatial-indicator: right)");
		add("on the left", "(spatial-indicator: left)");
		add("on right", "(spatial-indicator: right)");
		add("on left", "(spatial-indicator: left)");

		add("close to", "(spatial-indicator: near)");
		add("closest to", "(spatial-indicator: nearest)");
		add("near to", "(spatial-indicator: near)");
		add("nearest to", "(spatial-indicator: nearest)");
		add("next to", "(spatial-indicator: adjacent)");
		add("furthest from", "(spatial-indicator: furthest)");
		add("furthest away", "(spatial-indicator: furthest)");
		add("furthest away from", "(spatial-indicator: furthest)");
		add("farthest from", "(spatial-indicator: furthest)");
		add("farthest away", "(spatial-indicator: furthest)");
		add("farthest away from", "(spatial-indicator: furthest)");
		add("that is", "(link: that-is)");
		add("which is", "(link: that-is)");
		add("in between", "(spatial-indicator: between)");

		add("light blue", "(color: cyan)");
		add("sky blue", "(color: cyan)");
		add("dark blue", "(color: blue)");
		add("deep blue", "(color: blue)");
		add("light gray", "(color: white)");
		add("light grey", "(color: white)");
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
			if (token.tag.equals("end:")) {
				sentence = null;
			} else {
				if (sentence == null) {
					sentence = new Node("sentence:");
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
			if (!node.tag.equals("text:")) {
				continue;
			}

			// Lexicon.
			String value = node.getValue();
			Node match = Lexicon.get(value);
			if (match == null) {
				node.tag = "unknown:";
				continue;
			}

			// Apply.
			input.set(i, match);
		}

		// Clean.
		for (int i = input.size() - 1; i >= 0; i--) {
			Node item = input.get(i);
			if (item.hasTag("link:")
					|| (item.hasTag("description:") && item.getValue().equals(
							"definite"))) {
				input.remove(i);
			}
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