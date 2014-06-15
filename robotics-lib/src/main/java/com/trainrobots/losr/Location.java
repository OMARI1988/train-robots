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

public abstract class Location extends Losr {

	protected final Marker marker;
	protected final Losr item;

	protected Location(Marker marker, Losr item) {
		super(0, 0);
		this.marker = marker;
		this.item = item;
	}

	protected Location(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);

		// Item.
		int size = items.count();
		if (size == 1) {
			marker = null;
			item = items.get(0);
		}

		// Marker and item.
		else if (size == 2) {
			if (!(items.get(0) instanceof Marker)) {
				throw new RoboticException("%s is not a valid %s marker.",
						items.get(0), name());
			}
			marker = (Marker) items.get(0);
			item = items.get(1);
		}

		// Invalid.
		else {
			throw new RoboticException("Invalid %s items.", name());
		}

		// Verify item.
		if (!(item instanceof Entity) && !(item instanceof SpatialRelation)
				&& (!(item instanceof MeasureRelation))) {
			throw new RoboticException("Invalid %s item: %s.", name(), item);
		}
	}

	public Marker marker() {
		return marker;
	}

	public Losr item() {
		return item;
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Location) {
			Location location = (Location) losr;
			return location.id == id && location.referenceId == referenceId
					&& Objects.equals(location.marker, marker)
					&& location.item.equals(item);
		}
		return false;
	}

	@Override
	public int count() {
		return marker != null ? 2 : 1;
	}

	@Override
	public Losr get(int index) {
		int count = 0;
		if (marker != null && index == count++) {
			return marker;
		}
		if (index == count) {
			return item;
		}
		throw new IndexOutOfBoundsException();
	}
}