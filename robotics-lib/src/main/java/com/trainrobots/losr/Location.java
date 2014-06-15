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

	private final Marker marker;
	private final Entity entity;
	private final SpatialRelation spatialRelation;

	protected Location(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);

		// Items.
		Marker marker = null;
		Entity entity = null;
		SpatialRelation spatialRelation = null;
		for (Losr item : items) {

			// Marker.
			if (item instanceof Marker && marker == null) {
				marker = (Marker) item;
				continue;
			}

			// Spatial relation.
			if (item instanceof SpatialRelation && spatialRelation == null) {
				spatialRelation = (SpatialRelation) item;
				continue;
			}

			// Entity.
			if (item instanceof Entity && entity == null) {
				entity = (Entity) item;
				continue;
			}

			// Invalid.
			throw new RoboticException("Invalid %s item: %s.", name(), item);
		}

		// Location.
		if (entity == null && spatialRelation == null) {
			throw new RoboticException(
					"Either a spatial relation or an entity must be specified in a %s.",
					name());
		}
		if (entity != null && spatialRelation != null) {
			throw new RoboticException(
					"A spatial relation and entity can not be specified together in a %s.",
					name());
		}
		if (entity != null && marker == null) {
			throw new RoboticException(
					"An entity can not be specified without a marker in a %s.",
					name());
		}
		this.marker = marker;
		this.entity = entity;
		this.spatialRelation = spatialRelation;
	}

	public Marker marker() {
		return marker;
	}

	public Entity entity() {
		return entity;
	}

	public SpatialRelation spatialRelation() {
		return spatialRelation;
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Location) {
			Location location = (Location) losr;
			return location.id == id
					&& location.referenceId == referenceId
					&& Objects.equals(location.marker, marker)
					&& Objects.equals(location.entity, entity)
					&& Objects
							.equals(location.spatialRelation, spatialRelation);
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
			return entity != null ? entity : spatialRelation;
		}
		throw new IndexOutOfBoundsException();
	}
}