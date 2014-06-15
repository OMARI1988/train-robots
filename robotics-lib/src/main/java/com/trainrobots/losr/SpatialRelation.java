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

	private final Relation relation;
	private final Entity entity;

	public SpatialRelation(Relations relation, Types type) {
		this(new Relation(relation), new Entity(type));
	}

	public SpatialRelation(Relation relation, Entity entity) {
		this.relation = relation;
		this.entity = entity;
	}

	public SpatialRelation(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);

		// Items.
		Relation relation = null;
		Entity entity = null;
		for (Losr item : items) {

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

		// Spatial relation.
		if (relation == null) {
			throw new RoboticException("Relation not specified.");
		}
		if (entity == null) {
			throw new RoboticException("Entity not specified.");
		}
		this.relation = relation;
		this.entity = entity;
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
					&& spatialRelation.relation.equals(relation)
					&& Objects.equals(spatialRelation.entity, entity);
		}
		return false;
	}

	@Override
	public SpatialRelation clone() {
		return new SpatialRelation(relation.clone(),
				entity != null ? entity.clone() : null);
	}

	@Override
	public int count() {
		return 2;
	}

	@Override
	public Losr get(int index) {
		if (index == 0) {
			return relation;
		}
		if (index == 1) {
			return entity;
		}
		throw new IndexOutOfBoundsException();
	}
}