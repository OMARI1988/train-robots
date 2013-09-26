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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.CardinalAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.OrdinalAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.TypeAttribute;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class Chunker {

	private final Map<String, String> mappings = new HashMap<String, String>();
	private final List<Token> tokens = new ArrayList<Token>();

	public List<Token> getSeqence(String text) {

		// Tokens.
		tokens.clear();
		for (Node node : Tokenizer.getTokens(text).children) {
			tokens.add(new Token(node.getText(), "?"));
		}

		// Chunks.
		int size = tokens.size();
		for (int i = 0; i < size; i++) {
			String w = tokens.get(i).token;
			String mapping = mappings.get(w);
			tokens.get(i).tag = mapping != null ? mapping : "O";
		}
		return tokens;
	}

	public void train(List<Command> commands) {

		class F {
			String tag;
			int count;
		}

		Map<String, List<F>> map = new HashMap<String, List<F>>();

		for (Command command : commands) {
			Rcl rcl = command.rcl;
			if (rcl == null) {
				throw new CoreException("RCL not specified.");
			}
			for (Token token : getGoldSequence(command)) {

				String key = token.token;

				List<F> list = map.get(key);
				if (list == null) {
					list = new ArrayList<F>();
					map.put(key, list);
				}

				F m = null;
				for (F f : list) {
					if (f.tag.equals(token.tag)) {
						m = f;
					}
				}

				if (m == null) {
					m = new F();
					m.tag = token.tag;
					list.add(m);
				}
				m.count++;
			}
		}

		for (Map.Entry<String, List<F>> e : map.entrySet()) {
			String token = e.getKey();
			F b = null;
			for (F f : e.getValue()) {
				if (b == null || f.count > b.count) {
					b = f;
				}
			}
			mappings.put(token, b.tag);
		}
	}

	public static List<Token> getGoldSequence(Command command) {

		final List<Token> sequence = new ArrayList<Token>();

		for (Node node : Tokenizer.getTokens(command.text).children) {
			sequence.add(new Token(node.getValue(), "O"));
		}

		command.rcl.accept(new RclVisitor() {

			private boolean rel;

			public void visit(Entity entity) {
				rel = false;
			}

			public void visit(SpatialRelation spatialRelation) {
				rel = true;
			}

			public void visit(ActionAttribute attribute) {
				write(attribute, "ACT");
			}

			public void visit(ColorAttribute attribute) {
				write(attribute, "COLOR");
			}

			public void visit(IndicatorAttribute attribute) {
				write(attribute, rel ? "REL" : "IND");
			}

			public void visit(TypeAttribute attribute) {
				write(attribute, "TYPE");
			}

			public void visit(OrdinalAttribute attribute) {
				write(attribute, "ORD");
			}

			public void visit(CardinalAttribute attribute) {
				write(attribute, "CARD");
			}

			private void write(Rcl rcl, String tag) {

				int start = rcl.tokenStart();
				int end = rcl.tokenEnd();
				if (start == 0) {
					return;
				}
				for (int i = start; i <= end; i++) {
					sequence.get(i - 1).tag = i == start ? "B-" + tag : "I-"
							+ tag;
				}
			}
		});

		return sequence;
	}

}