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

public class Destination extends Losr {

	private final SpatialRelation spatialRelation;

	public Destination(SpatialRelation spatialRelation) {
		this.spatialRelation = spatialRelation;

	}

	public Destination(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);
		if (items.count() != 1) {
			throw new RoboticException("Invalid destination children.");
		}
		this.spatialRelation = (SpatialRelation) items.get(0);
	}

	public SpatialRelation spatialRelation() {
		return spatialRelation;
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Destination) {
			Destination destination = (Destination) losr;
			return destination.id == id
					&& destination.referenceId == referenceId
					&& destination.spatialRelation.equals(spatialRelation);
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
		return spatialRelation;
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("destination");
	}
}