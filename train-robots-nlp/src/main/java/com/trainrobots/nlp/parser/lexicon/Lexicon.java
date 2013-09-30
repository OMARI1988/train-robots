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

package com.trainrobots.nlp.parser.lexicon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.CardinalAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.OrdinalAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.core.rcl.RelationAttribute;
import com.trainrobots.core.rcl.TypeAttribute;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class Lexicon {

	private static final Lexicon goldLexicon;

	private List<Node> tokens;
	private final Map<String, LexiconEntry> entries = new HashMap<String, LexiconEntry>();

	static {
		List<Command> commands = new ArrayList<Command>();
		for (Command command : Corpus.getCommands()) {
			if (command.rcl != null) {
				commands.add(command);
			}
		}
		goldLexicon = new Lexicon(commands);
	}

	public static Lexicon goldLexicon() {
		return goldLexicon;
	}

	public Iterable<LexiconEntry> entries() {
		return entries.values();
	}

	public List<String> getMappings(String type, String token) {
		LexiconEntry entry = entries.get(token);
		if (entry == null) {
			return null;
		}
		return entry.getMappings(type);
	}

	public Lexicon(List<Command> commands) {

		for (Command command : commands) {
			tokens = Tokenizer.getTokens(command.text).children;
			command.rcl.recurse(new RclVisitor() {
				public void visit(Rcl parent, ActionAttribute attribute) {
					process(attribute);
				}

				public void visit(Rcl parent, ColorAttribute attribute) {
					process(attribute);
				}

				public void visit(Rcl parent, IndicatorAttribute attribute) {
					process(attribute);
				}

				public void visit(Rcl parent, RelationAttribute attribute) {
					process(attribute);
				}

				public void visit(Rcl parent, TypeAttribute attribute) {
					process(attribute);
				}

				public void visit(Rcl parent, OrdinalAttribute attribute) {
					process(attribute);
				}

				public void visit(Rcl parent, CardinalAttribute attribute) {
					process(attribute);
				}
			});
		}
	}

	private void process(Rcl rcl) {

		int start = rcl.tokenStart();
		int end = rcl.tokenEnd();
		if (start == 0) {
			return;
		}

		StringBuilder text = new StringBuilder();
		for (int i = start; i <= end; i++) {
			if (text.length() > 0) {
				text.append('_');
			}
			text.append(tokens.get(i - 1).getValue());
		}

		String token = text.toString();
		LexiconEntry entry = entries.get(token);
		if (entry == null) {
			entry = new LexiconEntry(token);
			entries.put(token, entry);
		}

		Node node = rcl.toNode();
		Mapping mapping = new Mapping(node.tag, node.getSingleLeaf());
		entry.add(mapping);
	}
}