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

public class SpatialRelation extends Losr {

	private final Measure measure;
	private final Relation relation;
	private final Entity entity;

	public SpatialRelation(Relations relation, Types type) {
		this(null, new Relation(relation), new Entity(type));
	}

	public SpatialRelation(Relation relation, Entity entity) {
		this(null, relation, entity);
	}

	public SpatialRelation(Measure measure, Relation relation, Entity entity) {
		this.measure = measure;
		this.relation = relation;
		this.entity = entity;
	}

	public SpatialRelation(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);

		// Items.
		Measure measure = null;
		Relation relation = null;
		Entity entity = null;
		for (Losr item : items) {

			// Measure.
			if (item instanceof Measure && measure == null) {
				measure = (Measure) item;
				continue;
			}

			// Relation.
			if (item instanceof Relation && relation == null) {
				relation = (Relation) item;
				continue;
			}

			// Entity.
			if (item instanceof Entity && entity == null) {
				entity = (Entity) item;
				continue;
			}

			// Invalid.
			throw new RoboticException("Invalid spatial-relation item: %s.",
					item);
		}

		// Event.
		if (relation == null) {
			throw new RoboticException("Relation not specified.");
		}
		this.measure = measure;
		this.relation = relation;
		this.entity = entity;
	}

	public Measure measure() {
		return measure;
	}

	public Relation relationAttribute() {
		return relation;
	}

	public Relations relation() {
		return relation.relation();
	}

	public Entity entity() {
		return entity;
	}

	@Override
	public String name() {
		return "spatial-relation";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof SpatialRelation) {
			SpatialRelation spatialRelation = (SpatialRelation) losr;
			return spatialRelation.id == id
					&& spatialRelation.referenceId == referenceId
					&& Objects.equals(spatialRelation.measure, measure)
					&& spatialRelation.relation.equals(relation)
					&& Objects.equals(spatialRelation.entity, entity);
		}
		return false;
	}

	@Override
	public int count() {
		int count = 1;
		if (measure != null) {
			count++;
		}
		if (entity != null) {
			count++;
		}
		return count;
	}

	@Override
	public Losr get(int index) {
		int count = 0;
		if (measure != null && index == count++) {
			return measure;
		}
		if (index == count++) {
			return relation;
		}
		if (entity != null && index == count) {
			return entity;
		}
		throw new IndexOutOfBoundsException();
	}
}