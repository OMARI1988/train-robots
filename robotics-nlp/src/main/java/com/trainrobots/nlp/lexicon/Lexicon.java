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
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import com.trainrobots.Log;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.MultiMap;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class Lexicon implements Iterable<Entry<String, Items<LexicalEntry>>> {

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
			Items<Terminal> tokens = command.tokens();
			int size = tokens.count();
			boolean[] used = new boolean[size];
			losr.visit(x -> {
				if (x instanceof Terminal) {
					Terminal terminal = (Terminal) x;
					TextContext context = terminal.context();
					if (context != null) {
						int start = context.start();
						int end = context.end();
						for (int i = start; i <= end; i++) {
							used[i - 1] = true;
						}
						add(tokens, context, terminal);
					}
				}
			});
			for (int i = 0; i < size; i++) {
				if (!used[i]) {
					add(tokens, tokens.get(i).context(), null);
				}
			}
		}

		// Calculate.
		for (Entry<String, Items<LexicalEntry>> pair : lexicon) {
			Items<LexicalEntry> entries = pair.getValue();

			// Sum by terminal type.
			Map<Class, Double> sumByType = new HashMap<Class, Double>();
			int size = entries.count();
			for (int i = 0; i < size; i++) {
				LexicalEntry entry = entries.get(i);
				Terminal terminal = entry.terminal();
				Class type = terminal != null ? terminal.getClass() : null;
				Double count = sumByType.get(type);
				sumByType.put(type, count == null ? entry.count() : count
						+ entry.count());
			}

			// Calculate p-values.
			for (int i = 0; i < size; i++) {
				LexicalEntry entry = entries.get(i);
				Terminal terminal = entry.terminal();
				Class type = terminal != null ? terminal.getClass() : null;
				entry.weight(entry.count() / sumByType.get(type));
			}
		}
	}

	@Override
	public Iterator<Entry<String, Items<LexicalEntry>>> iterator() {
		return lexicon.iterator();
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
			if (candidate.terminal() != null) {
				if ((type == null || candidate.terminal().getClass() == type)
						&& (entry == null || candidate.count() > entry.count())) {
					entry = candidate;
				}
			}
		}

		// Build.
		return entry != null ? (T) entry.terminal().withContext(context) : null;
	}

	public Items<LexicalEntry> entries(String key) {
		return lexicon.get(key);
	}

	private void add(Items<Terminal> tokens, TextContext context,
			Terminal terminal) {

		// Existing entry?
		LexicalEntry entry = null;
		String key = key(tokens, context);
		Items<LexicalEntry> entries = lexicon.get(key);
		if (entries != null) {
			int size = entries.count();
			for (int i = 0; i < size; i++) {
				LexicalEntry candidate = entries.get(i);
				if (Objects.equals(candidate.terminal(), terminal)) {
					entry = candidate;
					break;
				}
			}
		}

		// Create a new entry or increment existing count.
		if (entry == null) {
			lexicon.add(key, new LexicalEntry(terminal));
		} else {
			entry.count(entry.count() + 1);
		}
	}
}