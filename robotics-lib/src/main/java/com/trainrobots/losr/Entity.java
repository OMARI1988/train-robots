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

public class Entity extends Losr {

	private final Type type;

	public Entity(Types type) {
		this(new Type(type));
	}

	public Entity(Type type) {
		this.type = type;
	}

	public Entity(Items<Losr> items) {
		if (items.count() != 1) {
			throw new RoboticException("Invalid entity children.");
		}
		this.type = (Type) items.get(0);
	}

	public Type typeAttribute() {
		return type;
	}

	public Types type() {
		return type.type();
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Entity) {
			Entity entity = (Entity) losr;
			return entity.type.equals(type);
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
		return type;
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("entity");
	}
}