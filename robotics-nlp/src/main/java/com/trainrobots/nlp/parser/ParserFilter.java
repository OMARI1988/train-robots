/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.parser;

import com.trainrobots.collections.Items;
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

public class ParserFilter {

	private final AboveOrWithinRule aboveOrWithinRule = new AboveOrWithinRule();
	private final MarkerRule markerRule = new MarkerRule();
	private final Planner planner;
	private final Items<Terminal> tokens;

	public ParserFilter(Planner planner, Items<Terminal> tokens) {
		this.planner = planner;
		this.tokens = tokens;
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
		planner.instruction(losr);
		aboveOrWithinRule.validate(losr);
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