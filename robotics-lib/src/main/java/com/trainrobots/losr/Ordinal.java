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

	public Ordinal(TokenContext context, int value) {
		super(context);
		this.value = value;
	}

	public int value() {
		return value;
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
	protected void writeName(StringBuilder text) {
		text.append("ordinal");
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(value);
	}
}