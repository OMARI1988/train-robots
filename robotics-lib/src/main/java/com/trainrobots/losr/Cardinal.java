/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Cardinal extends Terminal {

	private final int value;

	public Cardinal(int value) {
		this(null, value);
	}

	public Cardinal(TokenContext context, int value) {
		super(context);
		this.value = value;
	}

	public int value() {
		return value;
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Cardinal) {
			Cardinal cardinal = (Cardinal) losr;
			return cardinal.id == id && cardinal.referenceId == referenceId
					&& cardinal.value == value;
		}
		return false;
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("cardinal");
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(value);
	}
}