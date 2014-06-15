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

public class MeasureRelation extends Losr {

	private final Measure measure;
	private final Losr item;

	public MeasureRelation(int id, int referenceId, Items<Losr> items) {
		super(id, referenceId);

		// Measure.
		if (items.count() != 2) {
			throw new RoboticException("Invalid measure-relation items.");
		}
		if (!(items.get(0) instanceof Measure)) {
			throw new RoboticException("%s is not a valid measure.",
					items.get(0));
		}
		measure = (Measure) items.get(0);

		// Item.
		item = items.get(1);
		if (!(item instanceof Destination)) {
			throw new RoboticException("Invalid measure-relation item: %s.",
					item);
		}
	}

	@Override
	public String name() {
		return "measure-relation";
	}

	public Measure measure() {
		return measure;
	}

	public Losr item() {
		return item;
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof MeasureRelation) {
			MeasureRelation measureRelation = (MeasureRelation) losr;
			return measureRelation.id == id
					&& measureRelation.referenceId == referenceId
					&& measureRelation.measure.equals(measure)
					&& measureRelation.item.equals(item);
		}
		return false;
	}

	@Override
	public int count() {
		return 2;
	}

	@Override
	public Losr get(int index) {
		if (index == 0) {
			return measure;
		}
		if (index == 1) {
			return item;
		}
		throw new IndexOutOfBoundsException();
	}
}