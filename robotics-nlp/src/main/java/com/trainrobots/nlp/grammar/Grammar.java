/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.grammar;

import com.trainrobots.Log;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.MultiMap;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.factory.LosrFactory;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class Grammar {

	private final MultiMap<String, Entry> rules = new MultiMap<String, Entry>();

	public Grammar(Treebank treebank) {

		// Diagnostics.
		Log.info("Building grammar...");

		// Build grammar.
		for (Command command : treebank.commands()) {
			Losr losr = command.losr();
			if (losr == null) {
				continue;
			}
			losr.visit(x -> {
				if (x.count() > 0) {
					add(x);
				}
			});
		}
	}

	public Losr nonTerminal(Items<Losr> items) {

		// Entries.
		Items<Entry> entries = rules.get(key(items));
		if (entries == null) {
			return null;
		}

		// Most frequent entry.
		Entry entry = null;
		for (Entry candidate : entries) {
			if (entry == null || candidate.count > entry.count) {
				entry = candidate;
			}
		}

		// Build.
		return entry != null ? LosrFactory.build(0, 0, entry.name, items)
				: null;
	}

	private void add(Losr losr) {

		// Key.
		String key = key(losr);

		// Name.
		String name = losr.name();

		// Entry.
		Items<Entry> entries = rules.get(key);
		Entry entry = null;
		if (entries != null) {
			for (Entry candidate : entries) {
				if (candidate.name.equals(name)) {
					entry = candidate;
					break;
				}
			}
		}
		if (entry == null) {
			entry = new Entry();
			entry.name = name;
			rules.add(key, entry);
		}
		entry.count++;
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

	private static class Entry {
		int count;
		String name;
	}
}