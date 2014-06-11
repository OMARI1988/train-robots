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

public class Destination extends Losr {

	private final Marker marker;
	private final Entity entity;
	private final SpatialRelation spatialRelation;

	public Destination(SpatialRelation spatialRelation) {
		this(null, spatialRelation);
	}

	public Destination(Marker marker, SpatialRelation spatialRelation) {
		this.marker = marker;
		this.entity = null;
		this.spatialRelation = spatialRelation;
	}

	public Destination(int id, int referenceId, Items<Losr> items) {
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
			throw new RoboticException("Invalid destination item: %s.", item);
		}

		// Destination.
		if (entity == null && spatialRelation == null) {
			throw new RoboticException(
					"Either a spatial relation or an entity must be specified in a destination.");
		}
		if (entity != null && spatialRelation != null) {
			throw new RoboticException(
					"A spatial relation and entity can not be specified together in a destination.");
		}
		if (entity != null && marker == null) {
			throw new RoboticException(
					"An entity can not be specified without a marker in a destination.");
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
	public String name() {
		return "destination";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Destination) {
			Destination destination = (Destination) losr;
			return destination.id == id
					&& destination.referenceId == referenceId
					&& Objects.equals(marker, marker)
					&& Objects.equals(entity, entity)
					&& Objects.equals(spatialRelation, spatialRelation);
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