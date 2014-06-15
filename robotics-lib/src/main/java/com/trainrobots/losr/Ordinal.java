/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Ordinal extends Terminal {

	private final int value;

	public Ordinal(int value) {
		this(null, value);
	}

	public Ordinal(TextContext context, int value) {
		super(context);
		this.value = value;
	}

	public int value() {
		return value;
	}

	@Override
	public String name() {
		return "ordinal";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Ordinal) {
			Ordinal ordinal = (Ordinal) losr;
			return ordinal.id == id && ordinal.referenceId == referenceId
					&& ordinal.value == value;
		}
		return false;
	}

	@Override
	public Ordinal clone() {
		return new Ordinal(context, value);
	}

	@Override
	public Ordinal withContext(TextContext context) {
		return new Ordinal(context, value);
	}

	@Override
	protected Object content() {
		return value;
	}
}