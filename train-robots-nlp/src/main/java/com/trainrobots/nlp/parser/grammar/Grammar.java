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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.nodes.Node;

public class Grammar {

	private static final Grammar goldGrammar;

	private final List<Node> leaves = new ArrayList<Node>();

	private final Map<String, ProductionRule> productionRuleMap = new HashMap<String, ProductionRule>();
	private final List<ProductionRule> productionRules = new ArrayList<ProductionRule>();

	private final Map<String, EllipsisRule> ellipsisRuleMap = new HashMap<String, EllipsisRule>();
	private final List<EllipsisRule> ellipsisRules = new ArrayList<EllipsisRule>();

	static {
		List<Command> commands = new ArrayList<Command>();
		for (Command command : Corpus.getCommands()) {
			if (command.rcl != null) {
				commands.add(command);
			}
		}
		goldGrammar = new Grammar(commands);
	}

	public static Grammar goldGrammar() {
		return goldGrammar;
	}

	public List<ProductionRule> productionRules() {
		return productionRules;
	}

	public List<EllipsisRule> ellipsisRules() {
		return ellipsisRules;
	}

	public Grammar(List<Command> commands) {
		for (Command command : commands) {
			processTree(command.rcl.toNode());
		}
		productionRules.addAll(productionRuleMap.values());
		ellipsisRules.addAll(ellipsisRuleMap.values());
	}

	private void processTree(Node root) {
		leaves.clear();
		findLeaves(root);
		findRules(root);
	}

	private void findLeaves(Node node) {

		// Recurse.
		for (Node child : node.children) {
			if (child.tag.equals("id:") || child.tag.equals("reference-id:")) {
				continue;
			}
			if (!isPhrase(child)) {
				leaves.add(child);
			} else {
				findLeaves(child);
			}
		}
	}

	private void findRules(Node node) {

		// Terminal?
		if (!isPhrase(node)) {
			return;
		}

		// Rule.
		ProductionRule rule = new ProductionRule(node.tag);
		Node ellipsis = null;
		Node first = null;
		for (Node child : node.children) {
			if (!child.tag.equals("id:") && !child.tag.equals("reference-id:")) {
				if (first == null) {
					first = child;
				}
				if (!isPhrase(child) && child.getChild("token:") == null) {
					rule.add("*" + child.tag);
					if (ellipsis != null) {
						throw new CoreException("Multiple ellipsis.");
					}
					ellipsis = child;
				} else {
					rule.add(child.tag);
				}
			}
		}

		// Context.
		if (ellipsis != null) {
			int i = leaves.indexOf(ellipsis);
			if (i < 0) {
				throw new CoreException("Leaf mismatch: " + first);
			}

			String before = leaves.get(i - 1).tag;
			String after = i + 1 < leaves.size() ? leaves.get(i + 1).tag : null;
			addEllipsis(before, ellipsis.tag, after);
		}

		// Add.
		String key = rule.toString();
		ProductionRule existingRule = productionRuleMap.get(key);
		if (existingRule != null) {
			existingRule.count++;
		} else {
			rule.count = 1;
			productionRuleMap.put(key, rule);
		}

		// Recurse.
		for (Node child : node.children) {
			findRules(child);
		}
	}

	private void addEllipsis(String before, String tag, String after) {
		EllipsisRule rule = new EllipsisRule(before, tag, after);
		String key = rule.toString();
		EllipsisRule existingRule = ellipsisRuleMap.get(key);
		if (existingRule != null) {
			existingRule.count++;
		} else {
			rule.count = 1;
			ellipsisRuleMap.put(key, rule);
		}
	}

	private static boolean isPhrase(Node node) {
		return node.tag.equals("event:") || node.tag.equals("sequence:")
				|| node.tag.equals("entity:")
				|| node.tag.equals("spatial-relation:")
				|| node.tag.equals("destination:")
				|| node.tag.equals("measure:");
	}
}