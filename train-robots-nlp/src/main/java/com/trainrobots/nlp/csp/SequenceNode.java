/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.nlp.csp;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.scenes.moves.Move;

public class SequenceNode implements ActionNode {

	private final Rcl rcl;
	private final Sequence sequence;

	public SequenceNode(Rcl rcl, Sequence sequence) {
		this.rcl = rcl;
		this.sequence = sequence;
	}

	public List<Move> solve(Model model) {

		// Recognized sequence?
		List<Move> moves = matchRecognizedSequence(model);
		if (moves != null) {
			return moves;
		}

		// Default sequence handling.
		moves = new ArrayList<Move>();
		for (Event event : sequence.events()) {
			moves.addAll(new EventNode(rcl, event).solve(model));
		}
		return moves;
	}

	private List<Move> matchRecognizedSequence(Model model) {

		// Events.
		List<Event> events = sequence.events();
		if (events.size() != 2) {
			return null;
		}

		// Take.
		Event event1 = events.get(0);
		if (!event1.isAction(Action.take) || event1.destinations() == null
				|| event1.destinations().size() > 0) {
			return null;
		}
		Entity entity1 = event1.entity();
		Integer id = entity1.id();
		if (id == null) {
			return null;
		}

		// Drop.
		Event event2 = events.get(1);
		if (!event2.isAction(Action.drop)) {
			return null;
		}
		Entity entity2 = event2.entity();
		if (!entity2.isType(Type.reference) || entity2.referenceId() == null
				|| !entity2.referenceId().equals(id)) {
			return null;
		}

		// Validate.
		if (entity2.relations() != null && entity2.relations().size() >= 1) {
			throw new CoreException("References must not have relations: "
					+ entity2);
		}

		// Translate equivalent move.
		Event event3 = new Event(new ActionAttribute(Action.move), entity1,
				event2.destinations());
		return new EventNode(event3, event3).solve(model);
	}
}