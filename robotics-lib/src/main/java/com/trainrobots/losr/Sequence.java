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

public class Sequence extends Losr {

	private final Event[] events;

	public Sequence(Event... events) {
		this.events = events;
	}

	public Sequence(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);

		// Events.
		int size = items.count();
		events = new Event[size];
		for (int i = 0; i < size; i++) {
			events[i] = (Event) items.get(i);
		}

		// Validate.
		if (events.length != 2) {
			throw new RoboticException("Expected two events.");
		}
		if (events[0].action() != Actions.Take
				|| events[1].action() != Actions.Drop) {
			throw new RoboticException(
					"Expected a take event followed by a drop event.");
		}
	}

	@Override
	public String name() {
		return "sequence";
	}

	@Override
	public boolean equals(Losr losr) {
		if (!(losr instanceof Sequence)) {
			return false;
		}
		Sequence sequence = (Sequence) losr;
		if (sequence.id != id || sequence.referenceId != referenceId) {
			return false;
		}
		int size = events.length;
		if (size != sequence.events.length) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (!events[i].equals(sequence.events[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Sequence clone() {
		int size = events.length;
		Event[] list = new Event[size];
		for (int i = 0; i < size; i++) {
			list[i] = events[i].clone();
		}
		return new Sequence(list);
	}

	@Override
	public int count() {
		return events.length;
	}

	@Override
	public Losr get(int index) {
		return events[index];
	}
}