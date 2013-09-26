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

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class Chunker {

	private final List<Expression> expressions = new ArrayList<Expression>();
	private final Map<String, String> tokenMap = new HashMap<String, String>();
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

			// Match expression?
			Expression expression = matchExpression(i);
			if (expression != null) {
				for (int k = 0; k < expression.tokens().length; k++) {
					String prefix = k == 0 ? "B-" : "I-";
					tokens.get(i + k).tag = prefix + expression.tag();
				}
				i += expression.tokens().length - 1;
			}

			// Default.
			else {
				String w = tokens.get(i).token;
				String tag = tokenMap.get(w);
				tokens.get(i).tag = tag != null ? tag : "O";
			}
		}
		return tokens;
	}

	private Expression matchExpression(int index) {

		Expression match = null;
		for (Expression expression : expressions) {
			if (matchExpression(index, expression)) {
				if (match == null
						|| expression.tokens().length > match.tokens().length) {
					match = expression;
					continue;
				}
				if (match.tokens().length == expression.tokens().length) {
					System.out.println("CONFLICT!! [" + match + "] vs ["
							+ expression + "]");
					continue;
				}
			}
		}

		return match;
	}

	private boolean matchExpression(int index, Expression expression) {
		String[] list = expression.tokens();
		for (int i = 0; i < list.length; i++) {
			if (index + i >= tokens.size()) {
				return false;
			}
			if (!list[i].equals(tokens.get(index + i).token)) {
				return false;
			}
		}
		return true;
	}

	public void train(List<Command> commands) {

		// Gold sequences.
		List<GoldSequence> goldSequences = new ArrayList<GoldSequence>();
		for (Command command : commands) {
			goldSequences.add(new GoldSequence(command));
		}

		// Expressions.
		buildExpressions(goldSequences);

		// Tag frequency.
		buildTagFrequency(goldSequences);
	}

	private void buildExpressions(List<GoldSequence> goldSequences) {

		Map<String, Expression> expressionMap = new HashMap<String, Expression>();

		for (GoldSequence goldSequence : goldSequences) {
			List<Token> tokens = goldSequence.tokens();
			int size = tokens.size();
			for (int i = 0; i < size; i++) {
				Token token = tokens.get(i);
				if (!token.tag.startsWith("B-")) {
					continue;
				}
				String tag = token.tag.substring(2);
				String inTag = "I-" + tag;

				int count = 1;
				for (int j = i + 1; j < size; j++) {
					if (!tokens.get(j).tag.equals(inTag)) {
						break;
					}
					count++;
				}
				if (count > 1) {

					String[] expressionTokens = new String[count];
					for (int k = 0; k < count; k++) {
						expressionTokens[k] = tokens.get(i + k).token;
					}

					String key = Expression.buildKey(expressionTokens);
					Expression existing = expressionMap.get(key);
					if (existing == null) {
						expressionMap.put(key,
								new Expression(goldSequence.command(), tag,
										expressionTokens));
					} else if (!existing.tag().equals(tag)) {
						System.out.println();
						System.out.println("CONFLICT [" + key + "]:");
						System.out.println("   " + existing.tag() + " in "
								+ existing.command().id + ": "
								+ existing.command().text);
						System.out.println("   " + tag + " in "
								+ goldSequence.command().id + ": "
								+ goldSequence.command().text);
					}
				}
			}
		}

		expressions.addAll(expressionMap.values());
	}

	private void buildTagFrequency(List<GoldSequence> goldSequences) {

		Map<String, List<TagFrequency>> tagFrequency = new HashMap<String, List<TagFrequency>>();

		for (GoldSequence goldSequence : goldSequences) {
			for (Token token : goldSequence.tokens()) {

				String key = token.token;

				List<TagFrequency> list = tagFrequency.get(key);
				if (list == null) {
					list = new ArrayList<TagFrequency>();
					tagFrequency.put(key, list);
				}

				TagFrequency m = null;
				for (TagFrequency f : list) {
					if (f.tag.equals(token.tag)) {
						m = f;
					}
				}

				if (m == null) {
					m = new TagFrequency();
					m.tag = token.tag;
					list.add(m);
				}
				m.count++;
			}
		}

		for (Map.Entry<String, List<TagFrequency>> e : tagFrequency.entrySet()) {
			String token = e.getKey();
			TagFrequency b = null;
			for (TagFrequency f : e.getValue()) {
				if (b == null || f.count > b.count) {
					b = f;
				}
			}
			tokenMap.put(token, b.tag);
		}
	}

	private static class TagFrequency {
		String tag;
		int count;
	}
}