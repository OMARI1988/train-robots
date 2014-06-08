/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr.reader;

import java.util.HashMap;
import java.util.Map;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Action;
import com.trainrobots.losr.Actions;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Destination;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Indicators;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Measure;
import com.trainrobots.losr.Ordinal;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.Sequence;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Symbol;
import com.trainrobots.losr.Text;
import com.trainrobots.losr.TextContext;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;

public class LosrFactory {

	private final Map<String, TerminalBuilder> terminals = new HashMap<>();
	private final Map<String, NonTerminalBuilder> nonTerminals = new HashMap<>();

	public LosrFactory() {

		// Terminals.
		terminal("text", Text::new);
		terminal("symbol", (t, c) -> new Symbol(t, c.charAt(0)));
		terminal("cardinal", (t, c) -> new Cardinal(t, Integer.parseInt(c)));
		terminal("ordinal", (t, c) -> new Ordinal(t, Integer.parseInt(c)));
		terminal("type", (t, c) -> new Type(t, Types.parse(c)));
		terminal("color", (t, c) -> new Color(t, Colors.parse(c)));
		terminal("action", (t, c) -> new Action(t, Actions.parse(c)));
		terminal("relation", (t, c) -> new Relation(t, Relations.parse(c)));
		terminal("indicator", (t, c) -> new Indicator(t, Indicators.parse(c)));

		// Non-terminals.
		nonTerminal("entity", Entity::new);
		nonTerminal("event", Event::new);
		nonTerminal("spatial-relation", SpatialRelation::new);
		nonTerminal("destination", Destination::new);
		nonTerminal("sequence", Sequence::new);
		nonTerminal("measure", Measure::new);
	}

	public Losr build(TextContext context, String name, String content) {

		// Builder.
		TerminalBuilder builder = terminals.get(name);
		if (builder == null) {
			throw new RoboticException(
					"The name '%s' is not recognized as a LOSR terminal.", name);
		}

		// Build.
		return builder.build(context, content);
	}

	public Losr build(int id, int referenceId, String name, Items<Losr> children) {

		// Builder.
		NonTerminalBuilder builder = nonTerminals.get(name);
		if (builder == null) {
			throw new RoboticException(
					"The name '%s' is not recognized as a LOSR node.", name);
		}

		// Build.
		return builder.build(id, referenceId, children);
	}

	private void terminal(String name, TerminalBuilder builder) {
		terminals.put(name, builder);
	}

	private void nonTerminal(String name, NonTerminalBuilder builder) {
		nonTerminals.put(name, builder);
	}
}