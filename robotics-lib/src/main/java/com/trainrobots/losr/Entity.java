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
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.collections.SingleItem;

public class Entity extends Losr {

	private final Items<Losr> items;

	public Entity(Types type) {
		this(0, 0, new SingleItem(new Type(type)));
	}

	public Entity(Types type, SpatialRelation spatialRelation) {
		this(0, 0, new ItemsArray(new Type(type), spatialRelation));
	}

	public Entity(Type type) {
		this(0, 0, new SingleItem(type));
	}

	public Entity(int id, Types type) {
		this(id, 0, new SingleItem(new Type(type)));
	}

	public Entity(Colors color, Types type) {
		this(0, 0, new ItemsArray(new Color(color), new Type(type)));
	}

	public Entity(Indicators indicator, Types type) {
		this(0, 0, new ItemsArray(new Indicator(indicator), new Type(type)));
	}

	public Entity(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);
		this.items = items;

		// Items.
		Cardinal cardinal = null;
		Type type = null;
		SpatialRelation spatialRelation = null;
		int size = items.count();
		for (int i = 0; i < size; i++) {
			Losr item = items.get(i);

			// Cardinal.
			if (item instanceof Cardinal && cardinal == null) {
				if (i != 0) {
					throw new RoboticException(
							"Expectec cardinality at start of entity.");
				}
				cardinal = (Cardinal) item;
				continue;
			}

			// Indicator.
			if (item instanceof Indicator) {
				continue;
			}

			// Color.
			if (item instanceof Color) {
				continue;
			}

			// Type.
			if (item instanceof Type && type == null) {
				type = (Type) item;
				continue;
			}

			// Spatial relation.
			if (item instanceof SpatialRelation && spatialRelation == null) {
				spatialRelation = (SpatialRelation) item;
				continue;
			}

			// Invalid.
			throw new RoboticException("Invalid entity item: %s.", item);
		}

		// Entity.
		if (type == null) {
			throw new RoboticException("Entity type not specified.");
		}

		// Validate reference.
		if (type.type() == Types.Reference && items.count() != 1) {
			throw new RoboticException("Invalid reference.");
		}

		// Validate region.
		if (type.type() == Types.Region && spatialRelation != null) {
			if (spatialRelation.relation() != Relations.Part
					|| spatialRelation.entity().type() != Types.Board) {
				throw new RoboticException("Region with spatial-relation.");
			}
		}
	}

	public Cardinal cardinal() {
		int size = items.count();
		for (int i = 0; i < size; i++) {
			Losr item = items.get(i);
			if (item instanceof Cardinal) {
				return (Cardinal) item;
			}
		}
		return null;
	}

	public Items<Indicators> indicators() {

		// Indicators.
		int size = items.count();
		ItemsList<Indicators> result = new ItemsList<>();
		for (int i = 0; i < size; i++) {
			Losr item = items.get(i);
			if (item instanceof Indicator) {
				result.add(((Indicator) item).indicator());
			}
		}
		return result.count() != 0 ? result : null;
	}

	public Items<Colors> colors() {

		// Unique colors.
		int size = items.count();
		ItemsList<Colors> result = new ItemsList<>();
		for (int i = 0; i < size; i++) {
			Losr item = items.get(i);
			if (item instanceof Color) {
				Colors color = ((Color) item).color();
				if (!result.contains(color)) {
					result.add(color);
				}
			}
		}
		return result.count() != 0 ? result : null;
	}

	public Types type() {
		int size = items.count();
		for (int i = 0; i < size; i++) {
			Losr item = items.get(i);
			if (item instanceof Type) {
				return ((Type) item).type();
			}
		}
		return null;
	}

	public SpatialRelation spatialRelation() {
		int size = items.count();
		for (int i = 0; i < size; i++) {
			Losr item = items.get(i);
			if (item instanceof SpatialRelation) {
				return (SpatialRelation) item;
			}
		}
		return null;
	}

	@Override
	public String name() {
		return "entity";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Entity) {
			Entity entity = (Entity) losr;
			return entity.id == id && entity.referenceId == referenceId
					&& Items.equals(entity.items, items);
		}
		return false;
	}

	@Override
	public Entity clone() {
		int size = items.count();
		Items[] list = new Items[size];
		for (int i = 0; i < size; i++) {
			list[i] = items.get(i).clone();
		}
		return new Entity(id, referenceId, new ItemsArray(list));
	}

	@Override
	public int count() {
		return items.count();
	}

	@Override
	public Losr get(int index) {
		return items.get(index);
	}
}