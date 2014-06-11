/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.MultiMap;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.factory.LosrFactory;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class Grammar {

	private final List<ProductionRule> productionRules = new ArrayList<>();
	private final List<EllipsisRule> ellipsisRules = new ArrayList<>();
	private final MultiMap<String, ProductionRule> rulesByRhs = new MultiMap<>();

	public Grammar(Treebank treebank) {

		// Diagnostics.
		Log.info("Building grammar...");

		// Build grammar.
		Set<String> productions = new HashSet<>();
		Set<String> ellipses = new HashSet<>();
		for (Command command : treebank.commands()) {
			Losr losr = command.losr();
			if (losr != null) {

				// Leaves.
				List<Terminal> leaves = new ArrayList<Terminal>();
				losr.visit(x -> {
					if (x instanceof Terminal) {
						leaves.add((Terminal) x);
					}
				});

				// Rules.
				losr.visit(x -> {
					if (!(x instanceof Terminal)) {
						addProduction(productions, leaves, x);
						addEllipsis(ellipses, leaves, x);
					}
				});
			}
		}

		// Diagnostics.
		Log.info("Production rules: %d", productionRules.size());
		Log.info("Ellipsis rules: %d", ellipsisRules.size());
	}

	public Iterable<ProductionRule> productionRules() {
		return productionRules;
	}

	public Iterable<EllipsisRule> ellipsisRules() {
		return ellipsisRules;
	}

	public Losr nonTerminal(Items<Losr> items) {

		// Rules.
		Items<ProductionRule> rules = rulesByRhs.get(key(items));
		if (rules == null) {
			return null;
		}

		// Most frequent rule.
		ProductionRule rule = null;
		int size = rules.count();
		for (int i = 0; i < size; i++) {
			ProductionRule candidate = rules.get(i);
			if (rule == null || candidate.frequency() > rule.frequency()) {
				rule = candidate;
			}
		}

		// Build.
		return rule != null ? LosrFactory.build(0, 0, rule.lhs(), items) : null;
	}

	private void addProduction(Set<String> productions, List<Terminal> leaves,
			Losr losr) {

		// Key.
		String key = key(losr);

		// New production rule?
		String lhs = losr.name();
		if (productions.add(lhs + ' ' + key)) {
			productionRules.add(new ProductionRule(losr));
		}

		// Update RHS lookup.
		Items<ProductionRule> rules = rulesByRhs.get(key);
		ProductionRule rule = null;
		if (rules != null) {
			int size = rules.count();
			for (int i = 0; i < size; i++) {
				ProductionRule candidate = rules.get(i);
				if (candidate.lhs().equals(lhs)) {
					rule = candidate;
					break;
				}
			}
		}
		if (rule == null) {
			rulesByRhs.add(key, rule = new ProductionRule(losr));
		}
		rule.frequency(rule.frequency() + 1);
	}

	private void addEllipsis(Set<String> ellipses, List<Terminal> leaves,
			Losr losr) {

		// Ellipsis?
		Losr ellipsis = null;
		int size = losr.count();
		for (int i = 0; i < size; i++) {
			Losr child = losr.get(i);
			if (child instanceof Terminal
					&& ((Terminal) child).context() == null) {
				if (ellipsis != null) {
					throw new RoboticException("Multiple ellipsis.");
				}
				ellipsis = child;
			}
		}
		if (ellipsis == null) {
			return;
		}

		// Context.
		int i = leaves.indexOf(ellipsis);
		String before = leaves.get(i - 1).name();
		String after = i + 1 < leaves.size() ? leaves.get(i + 1).name() : null;

		// Key.
		StringBuilder key = new StringBuilder();
		if (before != null) {
			key.append(before);
			key.append(' ');
		}
		key.append(ellipsis.name());
		if (after != null) {
			key.append(' ');
			key.append(after);
		}

		// Add rule.
		if (ellipses.add(key.toString())) {
			ellipsisRules.add(new EllipsisRule(before, ellipsis.name(), after));
		}
	}

	private static String key(Items<Losr> items) {
		StringBuilder text = new StringBuilder();
		int size = items.count();
		for (int i = 0; i < size; i++) {
			if (text.length() > 0) {
				text.append(' ');
			}
			text.append(items.get(i).name());
		}
		return text.toString();
	}
}