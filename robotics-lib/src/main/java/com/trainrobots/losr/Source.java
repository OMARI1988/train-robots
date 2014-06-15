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

public class Source extends Location {

	public Source(SpatialRelation spatialRelation) {
		super(0, 0, new SingleItem(spatialRelation));
	}

	public Source(Marker marker, SpatialRelation spatialRelation) {
		super(0, 0, new ItemsArray(marker, spatialRelation));
	}

	public Source(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId, items);
	}

	private Source(Marker marker, Losr item) {
		super(marker, item);
	}

	@Override
	public String name() {
		return "source";
	}

	@Override
	public Source clone() {
		return new Source(marker != null ? marker.clone() : null, item.clone());
	}
}