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

	public Event(Actions action, Types type) {
		this(new Action(action), new Entity(type));
	}

	public Event(Action action, Entity entity) {
		this.action = action;
		this.entity = entity;
	}

	public Event(Items<Losr> items) {
		if (items.count() != 2) {
			throw new RoboticException("Invalid event children.");
		}
		this.action = (Action) items.get(0);
		this.entity = (Entity) items.get(1);
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

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Event) {
			Event event = (Event) losr;
			return event.action.equals(action) && event.entity.equals(entity);
		}
		return false;
	}

	@Override
	public int count() {
		return 2;
	}

	@Override
	public Losr get(int index) {
		switch (index) {
		case 0:
			return action;
		case 1:
			return entity;
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("event");
	}
}