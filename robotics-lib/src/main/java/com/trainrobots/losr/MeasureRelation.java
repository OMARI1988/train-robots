/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import com.trainrobots.collections.Items;

public class MeasureRelation extends Losr {

	private final Items<Losr> items;

	public MeasureRelation(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);
		this.items = items;

		// TODO: VALIDATE ITEMS!!
	}

	@Override
	public String name() {
		return "measure-relation";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof MeasureRelation) {
			MeasureRelation measureRelation = (MeasureRelation) losr;
			return measureRelation.id == id
					&& measureRelation.referenceId == referenceId
					&& Items.equals(measureRelation.items, items);
		}
		return false;
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