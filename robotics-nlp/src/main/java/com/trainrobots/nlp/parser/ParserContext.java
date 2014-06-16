/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.parser;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.losr.Actions;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Location;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.nlp.validation.rules.AboveOrWithinRule;
import com.trainrobots.nlp.validation.rules.MarkerRule;
import com.trainrobots.planner.Planner;
import com.trainrobots.scenes.Layout;
import com.trainrobots.treebank.Command;

public class ParserContext {

	private final AboveOrWithinRule aboveOrWithinRule = new AboveOrWithinRule();
	private final MarkerRule markerRule = new MarkerRule();
	private final Planner planner;
	private final Items<Terminal> tokens;
	private final Command command;
	private boolean matchExpectedInstruction;

	public ParserContext(Command command) {
		this.planner = new Planner(command.scene().before());
		this.tokens = command.tokens();
		this.command = command;
	}

	public ParserContext(Layout layout, Items<Terminal> tokens) {
		this.planner = new Planner(layout);
		this.tokens = tokens;
		this.command = null;
	}

	public void matchExpectedInstruction(boolean matchExpectedInstruction) {
		this.matchExpectedInstruction = matchExpectedInstruction;
	}

	public Items<Terminal> tokens() {
		return tokens;
	}

	public void validatePartial(Losr losr) {
		if (losr instanceof Entity) {
			Entity e = (Entity) losr;
			if (e.spatialRelation() != null) {
				aboveOrWithinRule.validate(e, e.spatialRelation());
			}
		}
		if (losr instanceof Location) {
			markerRule.validate(losr, tokens);
		}
	}

	public void validateResult(Losr losr) {
		aboveOrWithinRule.validate(losr);
		Instruction instruction = planner.instruction(losr);
		if (matchExpectedInstruction) {
			if (!instruction.equals(command.scene().instruction())) {
				throw new RoboticException(
						"Failed to match expected instruction.");
			}
		}
	}

	public boolean better(Candidate candidate1, Candidate candidate2) {

		// LOSR.
		Losr losr1 = candidate1.losr();
		Losr losr2 = candidate2.losr();

		// Span.
		TextContext span1 = losr1.span();
		TextContext span2 = losr2.span();
		if (span1.end() > span2.end()) {
			return true;
		}

		// Events.
		if (losr1 instanceof Event && losr2 instanceof Event) {
			Event event1 = (Event) losr1;
			Event event2 = (Event) losr2;

			// Drop events.
			if (event1.action() == Actions.Drop
					&& event2.action() == Actions.Drop) {
				if (event1.destination() != null
						&& event2.destination() == null) {
					return true;
				}
			}
		}

		// Weight.
		return candidate1.weight() > candidate2.weight();
	}
}