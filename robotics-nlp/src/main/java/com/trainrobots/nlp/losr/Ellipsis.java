/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.losr;

import com.trainrobots.NotImplementedException;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;

public class Ellipsis extends Terminal {

	private final int after;

	public Ellipsis(int after) {
		super(null);
		this.after = after;
	}

	public int after() {
		return after;
	}

	@Override
	public String name() {
		return "ellipsis";
	}

	@Override
	public boolean equals(Losr losr) {
		throw new NotImplementedException();
	}

	@Override
	public Terminal withContext(TextContext context) {
		throw new NotImplementedException();
	}
}