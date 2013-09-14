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

package com.trainrobots.nlp.processor;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.grounding.Grounder;
import com.trainrobots.nlp.grounding.Grounding;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Shape;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.TakeMove;

public class Processor {

	private final Grounder grounder;

	public Processor(WorldModel world) {
		grounder = new Grounder(world);
	}

	public List<Move> getMoves(Rcl rcl) {

		// Sequence.
		List<Move> moves = new ArrayList<Move>();
		if (rcl instanceof Sequence) {
			for (Event event : ((Sequence) rcl).events()) {
				moves.add(getMove(event));
			}
		}

		// Event.
		else {
			if (!(rcl instanceof Event)) {
				throw new CoreException("Expected an RCL event.");
			}
			moves.add(getMove((Event) rcl));
		}
		return moves;
	}

	private Move getMove(Event event) {

		// Take.
		if (event.action() == Action.take) {
			return mapTakeCommand(event);
		}

		// Move.
		if (event.action() == Action.move) {
			return mapMoveCommand(event);
		}

		// No match.
		throw new CoreException("Event action '" + event.action()
				+ "' not recognized.");
	}

	private Move mapTakeCommand(Event event) {

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		return new TakeMove(getPosition(mapEntity(entity)));
	}

	private Move mapMoveCommand(Event event) {

		Entity entity = event.entity();
		if (entity == null) {
			throw new CoreException("Event entity not specified.");
		}

		Position position = getPosition(mapEntity(entity));

		if (event.destinations() == null || event.destinations().size() != 1) {
			throw new CoreException("Single destination not specified.");
		}

		SpatialRelation destination = event.destinations().get(0);
		Position position2 = mapSpatialRelation(destination);
		return new DirectMove(position, position2);
	}

	private Position mapSpatialRelation(SpatialRelation relation) {

		SpatialIndicator indicator = relation.indicator();
		if (indicator == null) {
			throw new CoreException("Indicator not specified.");
		}

		WorldEntity entity = mapEntity(relation.entity());
		Position position = getPosition(entity);

		switch (indicator) {
		case above:
			return entity.type() == Type.corner ? position : position.add(0, 0,
					1);
		case within:
			return position;
		default:
			throw new CoreException("Invalid indicator: " + indicator);
		}
	}

	private WorldEntity mapEntity(Entity entity) {
		List<Grounding> groundings = grounder.ground(entity);
		if (groundings.size() != 1) {
			throw new CoreException("Failed to ground: " + entity);
		}
		return groundings.get(0).entity();
	}

	private static Position getPosition(WorldEntity entity) {

		if (entity instanceof Shape) {
			return ((Shape) entity).position();
		}

		if (entity instanceof Corner) {
			return ((Corner) entity).position();
		}

		throw new CoreException("Failed to get position for " + entity);
	}
}