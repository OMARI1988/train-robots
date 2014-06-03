/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;

public class Event extends Losr {

	private final Action action;
	private final Entity entity;
	private final Destination destination;

	public Event(Actions action, Types type) {
		this(new Action(action), new Entity(type), null);
	}

	public Event(int id, Actions action, Types type) {
		this(new Action(action), new Entity(type), null);
	}

	public Event(Actions action, Types type, Destination destination) {
		this(new Action(action), new Entity(type), destination);
	}

	public Event(Action action, Entity entity) {
		this(action, entity, null);
	}

	public Event(Action action, Entity entity, Destination destination) {
		this.action = action;
		this.entity = entity;
		this.destination = destination;
	}

	public Event(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);

		// Items.
		Action action = null;
		Entity entity = null;
		Destination destination = null;
		for (Losr item : items) {

			// Action.
			if (item instanceof Action && action == null) {
				action = (Action) item;
				continue;
			}

			// Entity.
			if (item instanceof Entity && entity == null) {
				entity = (Entity) item;
				continue;
			}

			// Destination.
			if (item instanceof Destination && destination == null) {
				destination = (Destination) item;
				continue;
			}

			// Invalid.
			throw new RoboticException("Invalid event item: %s.", item);
		}

		// Event.
		if (action == null) {
			throw new RoboticException("Event action not specified.");
		}
		this.action = action;
		this.entity = entity;
		this.destination = destination;
	}

	public Action actionAttribute() {
		return action;
	}

	public Actions action() {
		return action.action();
	}

	public Entity entity() {
		return entity;
	}

	public Destination destinationAttribute() {
		return destination;
	}

	public SpatialRelation destination() {
		return destination.spatialRelation();
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Event) {
			Event event = (Event) losr;
			return event.id == id && event.referenceId == referenceId
					&& event.action.equals(action)
					&& event.entity.equals(entity);
		}
		return false;
	}

	@Override
	public int count() {
		return destination != null ? 3 : 2;
	}

	@Override
	public Losr get(int index) {
		switch (index) {
		case 0:
			return action;
		case 1:
			return entity;
		case 2:
			if (destination != null) {
				return destination;
			}
			break;
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("event");
	}
}