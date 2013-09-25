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

package com.trainrobots.nlp.chunker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class Chunker {

	private static Map<String, String> mappings = new HashMap<String, String>();

	private final List<Token> tokens = new ArrayList<Token>();

	private Chunker(String text) {
		for (Node node : Tokenizer.getTokens(text).children) {
			tokens.add(new Token(node.getText(), "?"));
		}
	}

	public static List<Token> getSeqence(String text) {
		Chunker chunker = new Chunker(text);
		chunker.process();
		return chunker.tokens;
	}

	private void process() {
		int size = tokens.size();
		for (int i = 0; i < size; i++) {

			String w = tokens.get(i).token;
			String w2 = i < size - 1 ? tokens.get(i + 1).token : null;
			String w3 = i < size - 2 ? tokens.get(i + 2).token : null;
			String w4 = i < size - 3 ? tokens.get(i + 3).token : null;

			Token l1 = i > 0 ? tokens.get(i - 1) : null;

			tokens.get(i).tag = classify(l1, w, w2, w3, w4);
		}
	}

	private String classify(Token l1, String w, String w2, String w3, String w4) {

		// Overrides.
		// if (w.equals("one") && l1 != null && l1.tag.equals("B-COLOR")) {
		// return "B-TYPE";
		// }
		// if (match2("top", "of", w, w2)) {
		// return "B-REL";
		// }

		// Default.
		String mapping = mappings.get(w);
		return mapping != null ? mapping : "O";
	}

	// private static boolean match2(String x1, String x2, String y1, String y2)
	// {
	// if (y1 == null || y2 == null) {
	// return false;
	// }
	// return x1.equals(y1) && x2.equals(y2);
	// }

	static {
		add("-", "O");
		add(",", "O");
		add(".", "O");
		add("1", "B-CARD");
		add("2", "B-CARD");
		add("3", "B-CARD");
		add("a", "O");
		add("above", "B-REL");
		add("an", "O");
		add("and", "O");
		add("at", "B-REL");
		add("atop", "B-REL");
		add("back", "B-IND");
		add("backwards", "B-IND");
		add("beside", "B-REL");
		add("block", "B-TYPE");
		add("blocks", "B-TYPE");
		add("blue", "B-COLOR");
		add("board", "O");
		add("border", "B-TYPE");
		add("bottom", "B-IND");
		add("botton", "B-IND");
		add("box", "B-TYPE");
		add("brick", "B-TYPE");
		add("cell", "B-TYPE");
		add("cells", "B-TYPE");
		add("center", "B-IND");
		add("centre", "B-IND");
		add("closer", "B-REL");
		add("closest", "B-REL");
		add("closet", "B-REL");
		add("combination", "B-TYPE");
		add("corner", "B-TYPE");
		add("cub", "B-TYPE");
		add("cube", "B-TYPE");
		add("cubes", "B-TYPE");
		add("cyan", "B-COLOR");
		add("dark", "B-COLOR");
		add("drop", "B-ACT");
		add("edge", "B-TYPE");
		add("far", "B-IND");
		add("floor", "B-TYPE");
		add("forward", "B-IND");
		add("four", "B-CARD");
		add("from", "O");
		add("front", "I-REL");
		add("grab", "B-ACT");
		add("gray", "B-COLOR");
		add("greed", "B-COLOR");
		add("green", "B-COLOR");
		add("grey", "B-COLOR");
		add("grid", "O");
		add("ground", "B-TYPE");
		add("hold", "B-ACT");
		add("in", "B-REL");
		add("into", "B-REL");
		add("is", "O");
		add("it", "B-TYPE");
		add("left", "B-IND");
		add("leftmost", "B-IND");
		add("lift", "B-ACT");
		add("light", "B-COLOR");
		add("located", "B-REL");
		add("lower", "B-ACT");
		add("magenta", "B-COLOR");
		add("margin", "B-TYPE");
		add("most", "I-IND");
		add("move", "B-ACT");
		add("near", "B-REL");
		add("nearest", "B-REL");
		add("next", "B-REL");
		add("of", "I-REL");
		add("on", "B-REL");
		add("one", "B-CARD");
		add("ones", "B-TYPE");
		add("onto", "B-REL");
		add("over", "B-REL");
		add("parallelipiped", "B-TYPE");
		add("pick", "B-ACT");
		add("pickup", "B-ACT");
		add("piece", "B-TYPE");
		add("pillar", "B-TYPE");
		add("pink", "B-COLOR");
		add("place", "B-ACT");
		add("placed", "B-REL");
		add("places", "B-TYPE");
		add("prism", "B-TYPE");
		add("pryamid", "B-TYPE");
		add("purple", "B-COLOR");
		add("put", "B-ACT");
		add("pyramid", "B-TYPE");
		add("red", "B-COLOR");
		add("remove", "B-ACT");
		add("right", "B-IND");
		add("robot", "B-TYPE");
		add("shift", "B-ACT");
		add("side", "I-REL");
		add("single", "B-IND");
		add("sitting", "B-REL");
		add("sky", "B-COLOR");
		add("slab", "B-TYPE");
		add("space", "B-TYPE");
		add("spots", "B-TYPE");
		add("square", "B-TYPE");
		add("squares", "B-TYPE");
		add("stack", "B-TYPE");
		add("standing", "B-REL");
		add("step", "B-TYPE");
		add("steps", "B-TYPE");
		add("take", "B-ACT");
		add("tetrahedron", "B-TYPE");
		add("that", "O");
		add("the", "O");
		add("this", "B-TYPE");
		add("three", "B-CARD");
		add("to", "O");
		add("top", "I-REL");
		add("towards", "B-IND");
		add("tower", "B-TYPE");
		add("transfer", "B-ACT");
		add("triangle", "B-TYPE");
		add("turquoise", "B-COLOR");
		add("two", "B-CARD");
		add("up", "I-ACT");
		add("which", "O");
		add("white", "B-COLOR");
		add("yellow", "B-COLOR");
		add("you", "B-TYPE");
	}

	static void add(String text, String tag) {
		mappings.put(text, tag);
	}
}