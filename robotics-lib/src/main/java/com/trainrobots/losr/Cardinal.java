/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Cardinal extends Token {

	private final int value;

	public Cardinal(String text, int value) {
		super(text);
		this.value = value;
	}

	public int value() {
		return value;
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Cardinal) {
			Cardinal cardinal = (Cardinal) losr;
			return cardinal.text.equals(text) && cardinal.value == value;
		}
		return false;
	}

	@Override
	public void write(StringBuilder text) {
		text.append("(cardinal: ");
		text.append(this.text);
		text.append(')');
	}
}