/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Ordinal extends Token {

	private final int value;

	public Ordinal(String text, int value) {
		super(text);
		this.value = value;
	}

	public int value() {
		return value;
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Ordinal) {
			Ordinal ordinal = (Ordinal) losr;
			return ordinal.text.equals(text) && ordinal.value == value;
		}
		return false;
	}

	@Override
	public void write(StringBuilder text) {
		text.append("(ordinal: ");
		text.append(this.text);
		text.append(')');
	}
}