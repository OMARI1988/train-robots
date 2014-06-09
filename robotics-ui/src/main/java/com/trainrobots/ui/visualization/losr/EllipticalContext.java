/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.losr;

import com.trainrobots.NotImplementedException;
import com.trainrobots.losr.TextContext;

public class EllipticalContext extends TextContext {

	private final int after;

	public EllipticalContext(int after) {
		super(0, 0);
		this.after = after;
	}

	public int after() {
		return after;
	}

	@Override
	public String text() {
		throw new NotImplementedException();
	}

	@Override
	public int start() {
		throw new NotImplementedException();
	}

	@Override
	public int end() {
		throw new NotImplementedException();
	}

	@Override
	public boolean equals(Object object) {
		throw new NotImplementedException();
	}

	@Override
	public String toString() {
		throw new NotImplementedException();
	}
}