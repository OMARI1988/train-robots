/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.collections.SingleItem;

public class Destination extends Location {

	public Destination(SpatialRelation spatialRelation) {
		super(0, 0, new SingleItem(spatialRelation));
	}

	public Destination(Marker marker, SpatialRelation spatialRelation) {
		super(0, 0, new ItemsArray(marker, spatialRelation));
	}

	public Destination(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId, items);
	}

	private Destination(Marker marker, Losr item) {
		super(marker, item);
	}

	@Override
	public String name() {
		return "destination";
	}

	@Override
	public Destination clone() {
		return new Destination(marker != null ? marker.clone() : null,
				item.clone());
	}
}