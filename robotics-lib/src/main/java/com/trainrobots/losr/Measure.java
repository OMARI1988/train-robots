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

public class Measure extends Losr {

	private final Entity entity;

	public Measure(Entity entity) {
		this.entity = entity;
	}

	public Measure(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);
		if (items.count() != 1) {
			throw new RoboticException("Invalid measure children.");
		}
		this.entity = (Entity) items.get(0);
	}

	public Entity entity() {
		return entity;
	}

	@Override
	public String name() {
		return "measure";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Measure) {
			Measure measure = (Measure) losr;
			return measure.id == id && measure.referenceId == referenceId
					&& measure.entity.equals(entity);
		}
		return false;
	}

	@Override
	public int count() {
		return 1;
	}

	@Override
	public Losr get(int index) {
		if (index != 0) {
			throw new IndexOutOfBoundsException();
		}
		return entity;
	}
}