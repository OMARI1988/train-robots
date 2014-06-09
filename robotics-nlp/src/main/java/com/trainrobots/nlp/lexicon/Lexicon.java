/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.lexicon;

import static com.trainrobots.nlp.lexicon.LexicalKey.key;

import com.trainrobots.Log;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.MultiMap;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class Lexicon {

	private final MultiMap<String, Entry> lexicon = new MultiMap<String, Entry>();

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
	}

	public <T extends Terminal> T terminal(Class<T> type, String key,
			TextContext context) {

		// Entries.
		Items<Entry> entries = lexicon.get(key);
		if (entries == null) {
			return null;
		}

		// Most frequent entry.
		Entry entry = null;
		for (Entry candidate : entries) {
			if ((type == null || candidate.terminal.getClass() == type)
					&& (entry == null || candidate.count > entry.count)) {
				entry = candidate;
			}
		}

		// Build.
		return entry != null ? (T) entry.terminal.withContext(context) : null;
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
		Items<Entry> entries = lexicon.get(key);
		Entry entry = null;
		if (entries != null) {
			for (Entry candidate : entries) {
				if (candidate.terminal.equals(terminal)) {
					entry = candidate;
					break;
				}
			}
		}
		if (entry == null) {
			entry = new Entry();
			entry.terminal = terminal;
			lexicon.add(key, entry);
		}
		entry.count++;
	}

	private static class Entry {
		int count;
		Terminal terminal;
	}
}