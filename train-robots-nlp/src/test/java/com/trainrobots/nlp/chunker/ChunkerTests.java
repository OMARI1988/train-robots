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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
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

public class ChunkerTests {

	@Test
	public void shouldEvaluateChunker() {

		int count = 0;
		int valid = 0;

		for (Command command : Corpus.getCommands()) {
			Rcl rcl = command.rcl;
			if (rcl == null) {
				continue;
			}

			List<Token> goldSequence = getGoldSequence(command);
			List<Token> predSequence = Chunker.getSeqence(command.text);

			int size = goldSequence.size();
			if (match(goldSequence, predSequence)) {
				count += size;
				valid += size;
			} else {

				System.out.println("// Command " + command.id);

				for (int i = 0; i < size; i++) {
					Token gold = goldSequence.get(i);
					Token pred = predSequence.get(i);
					System.out.print(gold.token + "\t" + gold.tag + "\t"
							+ pred.tag);
					if (!gold.tag.equals(pred.tag)) {
						System.out.print(" // ERROR");
					}
					System.out.println();
					if (gold.tag.equals(pred.tag)) {
						valid++;
					}
					count++;
				}
				System.out.println();
			}
		}

		// Score.
		DecimalFormat df = new DecimalFormat("#.##");
		double p = 100 * valid / (double) count;
		System.out.println("Score: " + valid + " / " + count + " = "
				+ df.format(p) + " %");
	}

	private static boolean match(List<Token> goldSequence,
			List<Token> predSequence) {
		int size = goldSequence.size();
		if (predSequence.size() != size) {
			throw new CoreException("Tokenization mismatch.");
		}
		for (int i = 0; i < size; i++) {
			if (!goldSequence.get(i).tag.equals(predSequence.get(i).tag)) {
				return false;
			}
		}
		return true;
	}

	@Test
	@Ignore
	public void shouldCalculateMappings() {

		class F {
			String tag;
			int count;
		}

		Map<String, List<F>> map = new HashMap<String, List<F>>();

		for (Command command : Corpus.getCommands()) {
			Rcl rcl = command.rcl;
			if (rcl == null) {
				continue;
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
			System.out.println("add(\"" + token + "\", \"" + b.tag + "\");");
		}
	}

	private List<Token> getGoldSequence(Command command) {

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