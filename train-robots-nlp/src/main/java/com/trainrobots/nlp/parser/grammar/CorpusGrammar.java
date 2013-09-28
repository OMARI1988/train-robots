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

package com.trainrobots.nlp.parser.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.nodes.Node;

public class CorpusGrammar {

	private static final List<GrammarRule> rules;

	private CorpusGrammar() {
	}

	static {
		Map<String, GrammarRule> map = new HashMap<String, GrammarRule>();
		for (Command command : Corpus.getCommands()) {
			if (command.rcl != null) {
				add(map, command.rcl.toNode());
			}
		}
		rules = new ArrayList<GrammarRule>(map.values());
	}

	public static List<GrammarRule> rules() {
		return rules;
	}

	private static void add(Map<String, GrammarRule> map, Node node) {

		// Terminal?
		if (!node.tag.equals("event:") && !node.tag.equals("sequence:")
				&& !node.tag.equals("entity:")
				&& !node.tag.equals("spatial-relation:")
				&& !node.tag.equals("destination:")
				&& !node.tag.equals("measure:")) {
			return;
		}

		// Rule.
		GrammarRule rule = new GrammarRule(node.tag);
		for (Node child : node.children) {
			if (!child.tag.equals("id:") && !child.tag.equals("reference-id:")) {
				rule.add(child.tag);
			}
		}
		String key = rule.toString();
		GrammarRule existingRule = map.get(key);
		if (existingRule != null) {
			existingRule.count++;
		} else {
			rule.count = 1;
			map.put(key, rule);
		}

		// Recurse.
		for (Node child : node.children) {
			add(map, child);
		}
	}
}