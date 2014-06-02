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
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Ordinal;
import com.trainrobots.losr.Symbol;
import com.trainrobots.losr.Text;
import com.trainrobots.losr.TokenContext;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;

public class LosrFactory {

	private final Map<String, TerminalBuilder> terminals = new HashMap<>();
	private final Map<String, NonTerminalBuilder> nonTerminals = new HashMap<>();

	public LosrFactory() {

		// Terminals.
		terminals.put("text", Text::new);
		terminals.put("symbol", Symbol::new);
		terminals.put("cardinal", Cardinal::new);
		terminals.put("ordinal", Ordinal::new);
		terminals.put("type", (t, c) -> new Type(t, Types.parse(c)));
		terminals.put("color", (t, c) -> new Color(t, Colors.parse(c)));
		terminals.put("action", (t, c) -> new Action(t, Actions.parse(c)));

		// Non-terminals.
		nonTerminals.put("entity", Entity::new);
		nonTerminals.put("event", Event::new);
	}

	public Losr build(TokenContext context, String name, String content) {

		// Builder.
		TerminalBuilder builder = terminals.get(name);
		if (builder == null) {
			throw new RoboticException(
					"The LOSR terminal name '%s' is not recognized.", name);
		}

		// Build.
		return builder.build(context, content);
	}

	public Losr build(String name, Items<Losr> children) {

		// Builder.
		NonTerminalBuilder builder = nonTerminals.get(name);
		if (builder == null) {
			throw new RoboticException(
					"The LOSR node name '%s' is not recognized.", name);
		}

		// Build.
		return builder.build(children);
	}
}