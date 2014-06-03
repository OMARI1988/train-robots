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

	private final Color color;
	private final Type type;

	public Entity(Types type) {
		this(new Type(type));
	}

	public Entity(Type type) {
		this(null, type);
	}

	public Entity(Colors color, Types type) {
		this(new Color(color), new Type(type));
	}

	public Entity(Color color, Type type) {
		this.color = color;
		this.type = type;
	}

	public Entity(Items<Losr> items) {

		// Items.
		Color color = null;
		Type type = null;
		for (Losr item : items) {

			// Color.
			if (item instanceof Color && color == null) {
				color = (Color) item;
				continue;
			}

			// Type.
			if (item instanceof Type && type == null) {
				type = (Type) item;
				continue;
			}

			// Invalid.
			throw new RoboticException("Invalid entity item: %s.", item);
		}

		// Entity.
		this.color = color;
		this.type = type;
	}

	public Color colorAttribute() {
		return color;
	}

	public Colors color() {
		return color != null ? color.color() : null;
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
		return color != null ? 2 : 1;
	}

	@Override
	public Losr get(int index) {
		int count = 0;
		if (color != null && index == count++) {
			return color;
		}
		if (index == count++) {
			return type;
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("entity");
	}
}