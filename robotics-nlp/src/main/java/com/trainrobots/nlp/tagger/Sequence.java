/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.tagger;

import java.util.List;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.losr.Action;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Marker;
import com.trainrobots.losr.Ordinal;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.losr.Type;
import com.trainrobots.nlp.lexicon.LexicalKey;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.treebank.Command;

public class Sequence {

	private final Items<Terminal> terminals;
	private final String[] tags;

	public Sequence(Command command) {
		this(command.tokens(), command.losr());
	}

	public Sequence(Items<Terminal> terminals, List<String> hmmTags) {

		// Terminals.
		this.terminals = terminals;
		int size = terminals.count();
		tags = new String[size];
		for (int i = 0; i < size; i++) {
			tags[i] = hmmTags.get(i + 2);
		}

		// Tags.
		for (int i = 1; i < size; i++) {
			String last = tags[i - 1];
			String token = tags[i];
			if (token.startsWith("I-") && !last.equals(token)) {
				String tag = token.substring(2);
				String expected = "B-" + tag;
				if (!last.equals(expected)) {
					tags[i] = expected;
				}
			}
		}
	}

	public Sequence(Items<Terminal> terminals, Losr losr) {

		// Terminals.
		this.terminals = terminals;
		int size = terminals.count();
		tags = new String[size];
		for (int i = 0; i < size; i++) {
			tags[i] = "O";
			if (text(i).equals("the")) {
				tags[i] = "DET";
			}
		}

		// Tags.
		losr.visit(x -> {
			if (!(x instanceof Terminal)) {
				return;
			}
			Terminal t = (Terminal) x;
			if (t instanceof Action) {
				tag(t, "ACT");
			} else if (t instanceof Color) {
				tag(t, "COLOR");
			} else if (t instanceof Indicator) {
				tag(t, "IND");
			} else if (t instanceof Relation) {
				tag(t, "REL");
			} else if (t instanceof Type) {
				tag(t, "TYPE");
			} else if (t instanceof Ordinal) {
				tag(t, "ORD");
			} else if (t instanceof Cardinal) {
				tag(t, "CARD");
			} else if (t instanceof Marker) {
				tag(t, "MARK");
			}
		});
	}

	public Items<Terminal> terminals(Lexicon lexicon) {
		ItemsList<Terminal> result = new ItemsList<Terminal>();
		for (int i = 0; i < tags.length; i++) {

			// O
			String token = tags[i];
			if (token.equals("O") || token.equals("DET")) {
				continue;
			}

			// Sequence.
			else if (!token.startsWith("B-")) {
				return result; // Invalid sequence.
			}
			int size = 1;
			while (i + size < tags.length && tags[i + size].startsWith("I-")) {
				size++;
			}
			String tag = token.substring(2);
			TextContext context = new TextContext(i + 1, i + size);
			i += size - 1;
			Terminal terminal = terminal(lexicon, tag, context);
			if (terminal != null) {
				result.add(terminal);
			}
		}
		return result;
	}

	public int count() {
		return tags.length;
	}

	public String text(int index) {
		return terminals.get(index).context().text();
	}

	public String tag(int index) {
		return tags[index];
	}

	private Terminal terminal(Lexicon lexicon, String tag, TextContext context) {

		// Map.
		Class type;
		if (tag.equals("ACT")) {
			type = Action.class;
		} else if (tag.equals("COLOR")) {
			type = Color.class;
		} else if (tag.equals("IND")) {
			type = Indicator.class;
		} else if (tag.equals("REL")) {
			type = Relation.class;
		} else if (tag.equals("TYPE")) {
			type = Type.class;
		} else if (tag.equals("ORD")) {
			type = Ordinal.class;
		} else if (tag.equals("CARD")) {
			type = Cardinal.class;
		} else if (tag.equals("MARK")) {
			type = Marker.class;
		} else {
			throw new RoboticException("Failed to map tag '" + tag
					+ "' to LOSR.");
		}

		// Lexicon.
		String key = LexicalKey.key(terminals, context);
		return lexicon.terminal(type, key, context);
	}

	private void tag(Terminal terminal, String tag) {
		TextContext context = terminal.context();
		if (context != null) {
			int start = context.start();
			int end = context.end();
			for (int i = start; i <= end; i++) {
				tags[i - 1] = i == start ? "B-" + tag : "I-" + tag;
			}
		}
	}
}