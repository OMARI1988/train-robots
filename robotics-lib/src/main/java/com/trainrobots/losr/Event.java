/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.Objects;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;

public class Event extends Losr {

	private final Action action;
	private final Entity entity;
	private final Source source;
	private final Destination destination;

	public Event(Actions action, Types type) {
		this(new Action(action), new Entity(type), null, null);
	}

	public Event(int id, Actions action, Types type) {
		this(new Action(action), new Entity(type), null, null);
	}

	public Event(Actions action, Types type, Destination destination) {
		this(new Action(action), new Entity(type), null, destination);
	}

	public Event(Action action, Entity entity) {
		this(action, entity, null, null);
	}

	public Event(Action action, Entity entity, Source source,
			Destination destination) {
		this.action = action;
		this.entity = entity;
		this.source = source;
		this.destination = destination;
	}

	public Event(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);

		// Items.
		Action action = null;
		Entity entity = null;
		Source source = null;
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

			// Source.
			if (item instanceof Source && source == null) {
				source = (Source) item;
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
		this.source = source;
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

	public Source source() {
		return source;
	}

	public Destination destination() {
		return destination;
	}

	@Override
	public String name() {
		return "event";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Event) {
			Event event = (Event) losr;
			return event.id == id && event.referenceId == referenceId
					&& event.action.equals(action)
					&& event.entity.equals(entity)
					&& Objects.equals(event.source, source)
					&& Objects.equals(event.destination, destination);
		}
		return false;
	}

	@Override
	public Event clone() {
		return new Event(action.clone(), entity.clone(),
				source != null ? source.clone() : null,
				destination != null ? destination.clone() : null);
	}

	@Override
	public int count() {
		int count = 2;
		if (source != null) {
			count++;
		}
		if (destination != null) {
			count++;
		}
		return count;
	}

	@Override
	public Losr get(int index) {
		int count = 0;
		if (index == count++) {
			return action;
		}
		if (index == count++) {
			return entity;
		}
		if (source != null && index == count++) {
			return source;
		}
		if (destination != null && index == count) {
			return destination;
		}
		throw new IndexOutOfBoundsException();
	}
}