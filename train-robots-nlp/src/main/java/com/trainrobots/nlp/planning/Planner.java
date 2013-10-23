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

package com.trainrobots.nlp.planning;

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
import com.trainrobots.nlp.csp.CspConverter;
import com.trainrobots.nlp.csp.EventNode;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.Move;

public class Planner {

	private final Model model;

	public Planner(WorldModel world) {
		model = new Model(world);
	}

	public List<WorldEntity> ground(Rcl root, Entity entity) {
		return CspConverter.convert(root, entity).solve(model);
	}

	public List<Move> getMoves(Rcl rcl) {

		// Sequence.
		if (rcl instanceof Sequence) {

			// Recognized sequence?
			Sequence sequence = (Sequence) rcl;
			List<Move> moves = new ArrayList<Move>();
			Move move = matchRecognizedSequence(rcl, sequence);
			if (move != null) {
				moves.add(move);
			}

			// Default sequence handling.
			else {
				for (Event event : sequence.events()) {
					moves.add(EventNode.solve(model, rcl, event));
				}
			}
			return moves;
		}

		// Event.
		if (!(rcl instanceof Event)) {
			throw new CoreException("Expected an RCL event.");
		}
		List<Move> moves = new ArrayList<Move>();
		moves.add(EventNode.solve(model, rcl, (Event) rcl));
		return moves;
	}

	private Move matchRecognizedSequence(Rcl root, Sequence sequence) {

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
		return EventNode.solve(model, event3, event3);
	}
}