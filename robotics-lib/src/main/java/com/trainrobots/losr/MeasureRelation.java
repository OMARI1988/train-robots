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

		// Items.
		int size = items.count();
		Measure measure = null;
		Indicator indicator = null;
		Source source = null;
		Destination destination = null;
		SpatialRelation spatialRelation = null;
		for (int i = 0; i < size; i++) {
			Losr item = items.get(i);

			// Measure.
			if (item instanceof Measure && measure == null) {
				measure = (Measure) item;
				continue;
			}

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

		// Measure.
		if (measure == null) {
			throw new RoboticException("Measure not specified.");
		}
	}

	@Override
	public String name() {
		return "measure-relation";
	}

	public Measure measure() {
		int size = items.count();
		for (int i = 0; i < size; i++) {
			Losr item = items.get(i);
			if (item instanceof Measure) {
				return ((Measure) item);
			}
		}
		return null;
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