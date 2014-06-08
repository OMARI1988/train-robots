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
import com.trainrobots.collections.ItemsList;
import com.trainrobots.collections.SingleItem;

public class Entity extends Losr {

	private final Cardinal cardinal;
	private final Items<Indicator> indicators;
	private final Items<Color> colors;
	private final Type type;
	private final SpatialRelation spatialRelation;

	public Entity(Types type) {
		this(0, 0, null, null, null, new Type(type), null);
	}

	public Entity(Types type, SpatialRelation spatialRelation) {
		this(0, 0, null, null, null, new Type(type), spatialRelation);
	}

	public Entity(Type type) {
		this(0, 0, null, null, null, type, null);
	}

	public Entity(int id, Types type) {
		this(id, 0, null, null, null, new Type(type), null);
	}

	public Entity(Colors color, Types type) {
		this(0, 0, null, null, new SingleItem(new Color(color)),
				new Type(type), null);
	}

	public Entity(Indicators indicator, Types type) {
		this(0, 0, null, new SingleItem(new Indicator(indicator)), null,
				new Type(type), null);
	}

	public Entity(int id, int referenceId, Cardinal cardinal,
			Items<Indicator> indicators, Items<Color> colors, Type type,
			SpatialRelation spatialRelation) {
		super(id, referenceId);
		this.cardinal = cardinal;
		this.indicators = indicators;
		this.colors = colors;
		this.type = type;
		this.spatialRelation = spatialRelation;
	}

	public Entity(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);

		// Items.
		Cardinal cardinal = null;
		ItemsList<Indicator> indicators = null;
		ItemsList<Color> colors = null;
		Type type = null;
		SpatialRelation spatialRelation = null;
		for (Losr item : items) {

			// Cardinal.
			if (item instanceof Cardinal && cardinal == null) {
				cardinal = (Cardinal) item;
				continue;
			}

			// Indicator.
			if (item instanceof Indicator) {
				if (indicators == null) {
					indicators = new ItemsList<Indicator>();
				}
				indicators.add((Indicator) item);
				continue;
			}

			// Color.
			if (item instanceof Color) {
				if (colors == null) {
					colors = new ItemsList<Color>();
				}
				colors.add((Color) item);
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
		this.cardinal = cardinal;
		this.indicators = indicators;
		this.colors = colors;
		this.type = type;
		this.spatialRelation = spatialRelation;
	}

	public Cardinal cardinal() {
		return cardinal;
	}

	public Items<Indicator> indicators() {
		return indicators;
	}

	public Items<Color> colors() {
		return colors;
	}

	public Type typeAttribute() {
		return type;
	}

	public Types type() {
		return type.type();
	}

	public SpatialRelation spatialRelation() {
		return spatialRelation;
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
					&& Objects.equals(entity.cardinal, cardinal)
					&& Items.equals(entity.indicators, indicators)
					&& Items.equals(entity.colors, colors)
					&& entity.type.equals(type)
					&& Objects.equals(entity.spatialRelation, spatialRelation);
		}
		return false;
	}

	@Override
	public int count() {
		int count = 1;
		if (cardinal != null) {
			count++;
		}
		if (indicators != null) {
			count += indicators.count();
		}
		if (colors != null) {
			count += colors.count();
		}
		if (spatialRelation != null) {
			count++;
		}
		return count;
	}

	@Override
	public Losr get(int index) {
		int count = 0;
		if (cardinal != null && index == count++) {
			return cardinal;
		}
		if (indicators != null) {
			int size = indicators.count();
			int indicatorIndex = index - count;
			if (indicatorIndex >= 0 && indicatorIndex < size) {
				return indicators.get(indicatorIndex);
			}
			count += size;
		}
		if (colors != null) {
			int size = colors.count();
			int colorIndex = index - count;
			if (colorIndex >= 0 && colorIndex < size) {
				return colors.get(colorIndex);
			}
			count += size;
		}
		if (index == count++) {
			return type;
		}
		if (spatialRelation != null && index == count++) {
			return spatialRelation;
		}
		throw new IndexOutOfBoundsException();
	}
}