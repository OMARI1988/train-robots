/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import com.trainrobots.NotImplementedException;

public abstract class Terminal extends Losr {

	protected final TokenContext context;

	protected Terminal(TokenContext context) {
		this.context = context;
	}

	public TokenContext context() {
		return context;
	}

	@Override
	public int count() {
		return 0;
	}

	@Override
	public Losr get(int index) {
		throw new IndexOutOfBoundsException();
	}

	protected void writeName(StringBuilder text) {
		throw new NotImplementedException(
				"Terminal nodes do not supported this operation.");
	}
}