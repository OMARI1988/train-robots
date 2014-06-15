/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.lexicon;

import static com.trainrobots.nlp.lexicon.LexicalKey.key;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.trainrobots.Log;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.collections.MultiMap;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class Lexicon {

	private final MultiMap<String, LexicalEntry> lexicon = new MultiMap<>();

	public Lexicon(Treebank treebank) {

		// Diagnostics.
		Log.info("Building lexicon...");

		// Build lexicon.
		for (Command command : treebank.commands()) {
			Losr losr = command.losr();
			if (losr == null) {
				continue;
			}
			losr.visit(x -> {
				if (x instanceof Terminal) {
					add(command.tokens(), (Terminal) x);
				}
			});
		}

		// Calculate.
		for (Entry<String, Items<LexicalEntry>> pair : lexicon) {
			Items<LexicalEntry> entries = pair.getValue();

			// Sum by terminal type.
			Map<Class, Double> sumByType = new HashMap<Class, Double>();
			int size = entries.count();
			for (int i = 0; i < size; i++) {
				LexicalEntry entry = entries.get(i);
				Class type = entry.terminal().getClass();
				Double count = sumByType.get(type);
				sumByType.put(type, count == null ? entry.count() : count
						+ entry.count());
			}

			// Calculate p-values.
			for (int i = 0; i < size; i++) {
				LexicalEntry entry = entries.get(i);
				entry.weight(entry.count()
						/ sumByType.get(entry.terminal().getClass()));
			}
		}
	}

	public <T extends Terminal> T terminal(Class<T> type, String key,
			TextContext context) {

		// Entries.
		Items<LexicalEntry> entries = lexicon.get(key);
		if (entries == null) {
			return null;
		}

		// Most frequent entry.
		LexicalEntry entry = null;
		for (LexicalEntry candidate : entries) {
			if ((type == null || candidate.terminal().getClass() == type)
					&& (entry == null || candidate.count() > entry.count())) {
				entry = candidate;
			}
		}

		// Build.
		return entry != null ? (T) entry.terminal().withContext(context) : null;
	}

	public <T extends Terminal> Items<LexicalEntry> entries(Class<T> type,
			String key) {

		// Entries.
		Items<LexicalEntry> entries = lexicon.get(key);
		if (entries == null || type == null) {
			return entries;
		}

		// Match type.
		ItemsList<LexicalEntry> results = new ItemsList<>();
		int size = entries.count();
		for (int i = 0; i < size; i++) {
			LexicalEntry entry = entries.get(i);
			if (entry.terminal().getClass() == type) {
				results.add(entry);
			}
		}
		return results;
	}

	private void add(Items<Terminal> tokens, Terminal terminal) {

		// Context.
		TextContext context = terminal.context();
		if (context == null) {
			return;
		}

		// Key.
		String key = key(tokens, context);

		// Entry.
		Items<LexicalEntry> entries = lexicon.get(key);
		LexicalEntry entry = null;
		if (entries != null) {
			int size = entries.count();
			for (int i = 0; i < size; i++) {
				LexicalEntry candidate = entries.get(i);
				if (candidate.terminal().equals(terminal)) {
					entry = candidate;
					break;
				}
			}
		}
		if (entry == null) {
			lexicon.add(key, new LexicalEntry(terminal));
		} else {
			entry.count(entry.count() + 1);
		}
	}
}