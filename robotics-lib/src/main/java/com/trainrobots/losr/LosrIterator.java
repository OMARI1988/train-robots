/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.Iterator;

import com.trainrobots.NotImplementedException;

public class LosrIterator implements Iterator<Losr> {

	private final Losr losr;
	private int index;

	public LosrIterator(Losr losr) {
		this.losr = losr;
	}

	@Override
	public boolean hasNext() {
		return index < losr.count();
	}

	@Override
	public Losr next() {
		return losr.get(index++);
	}

	@Override
	public void remove() {
		throw new NotImplementedException(
				"A LOSR iterator can not remove elements.");
	}
}