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
import com.trainrobots.collections.ItemsArray;

public class MeasureRelation extends Losr {

	private final Items<Losr> items;

	public MeasureRelation(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);
		this.items = items;

		// Measure.
		if (!(items.get(0) instanceof Measure)) {
			throw new RoboticException("%s is not a valid measure.",
					items.get(0));
		}

		// Items.
		int size = items.count();
		Indicator indicator = null;
		Source source = null;
		Destination destination = null;
		SpatialRelation spatialRelation = null;
		for (int i = 1; i < size; i++) {
			Losr item = items.get(i);

			// Indicator.
			if (item instanceof Indicator && indicator == null) {
				indicator = (Indicator) item;
				continue;
			}

			// Source.
			if (item instanceof Source && source == null) {
				source = (Source) item;
				continue;
			}

			// Destination.
			if (item instanceof Destination && destination == null) {
				destination = (Destination) item;
				continue;
			}

			// Spatial relation.
			if (item instanceof SpatialRelation && spatialRelation == null) {
				spatialRelation = (SpatialRelation) item;
				continue;
			}

			// Invalid.
			throw new RoboticException("Invalid measure-relation item: %s.",
					item);
		}

	}

	@Override
	public String name() {
		return "measure-relation";
	}

	public Measure measure() {
		return (Measure) items.get(0);
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
	public MeasureRelation clone() {
		int size = items.count();
		Items[] list = new Items[size];
		for (int i = 0; i < size; i++) {
			list[i] = items.get(i).clone();
		}
		return new MeasureRelation(id, referenceId, new ItemsArray(list));
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