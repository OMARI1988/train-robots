/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Marker extends Terminal {

	public Marker() {
		super(null);
	}

	public Marker(TextContext context) {
		super(context);
	}

	@Override
	public String name() {
		return "marker";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Marker) {
			Marker marker = (Marker) losr;
			return marker.id == id && marker.referenceId == referenceId;
		}
		return false;
	}

	@Override
	public Marker withContext(TextContext context) {
		return new Marker(context);
	}
}