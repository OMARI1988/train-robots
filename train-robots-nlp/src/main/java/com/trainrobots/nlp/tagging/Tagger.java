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
import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.parser.partial.Lexicon;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class Tagger {

	private static final List<Chunk> chunks = new ArrayList<Chunk>();

	static {

		add("left hand", "(indicator: left)");
		add("right hand", "(indicator: right)");
		add("far right",
				"(indicator: front) (indicator: right)");
		add("far left", "(indicator: front) (indicator: left)");

		add("pick up", "(event: (action: take))");

		add("to the left bottom corner",
				"(indicator: to (entity: (indicator: left) (indicator: back) (type: corner)))");

		add("to the top left corner",
				"(indicator: to (entity: (indicator: front) (indicator: left) (type: corner)))");

		add("to the top right corner",
				"(indicator: to (entity: (indicator: front) (indicator: right) (type: corner)))");

		add("the left bottom corner",
				"(entity: (indicator: left) (indicator: back) (type: corner))");

		add("the top left corner",
				"(entity: (indicator: front) (indicator: left) (type: corner))");

		add("the top right corner",
				"(entity: (indicator: front) (indicator: right) (type: corner))");

		add("right in", "(indicator: in)");

		add("on the top of", "(indicator: above)");
		add("on top of", "(indicator: above)");
		add("the top of", "(indicator: above)");
		add("on to", "(indicator: above)");

		add("the nearest", "(attribute: nearest)");
		add("the farthest", "(attribute: furthest)");
		add("the furthest", "(attribute: furthest)");

		add("in front of", "(indicator: front)");

		add("to right of", "(indicator: right)");
		add("to left of", "(indicator: left)");
		add("to the right of", "(indicator: right)");
		add("to the left of", "(indicator: left)");
		add("to the position of", "(indicator: at)");
		add("to the position", "(indicator: at)");
		add("to your right", "(indicator: right)");
		add("to your left", "(indicator: left)");
		add("to the right", "(indicator: right)");
		add("to the left", "(indicator: left)");
		add("to right", "(indicator: right)");
		add("to left", "(indicator: left)");

		add("on right of", "(indicator: right)");
		add("on left of", "(indicator: left)");
		add("on the right of", "(indicator: right)");
		add("on the left of", "(indicator: left)");
		add("on the position of", "(indicator: at)");
		add("on the position", "(indicator: at)");
		add("on your right", "(indicator: right)");
		add("on your left", "(indicator: left)");
		add("on the right", "(indicator: right)");
		add("on the left", "(indicator: left)");
		add("on right", "(indicator: right)");
		add("on left", "(indicator: left)");

		add("close to", "(indicator: near)");
		add("closest to", "(indicator: nearest)");
		add("near to", "(indicator: near)");
		add("nearest to", "(indicator: nearest)");
		add("next to", "(indicator: adjacent)");
		add("furthest from", "(indicator: furthest)");
		add("furthest away", "(indicator: furthest)");
		add("furthest away from", "(indicator: furthest)");
		add("farthest from", "(indicator: furthest)");
		add("farthest away", "(indicator: furthest)");
		add("farthest away from", "(indicator: furthest)");
		add("that is", "(link: that-is)");
		add("which is", "(link: that-is)");
		add("in between", "(indicator: between)");

		add("light blue", "(color: cyan)");
		add("sky blue", "(color: cyan)");
		add("blue sky", "(color: cyan)");
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