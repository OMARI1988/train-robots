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

	public Cardinal(String text) {
		this(text, Integer.parseInt(text));
	}

	public Cardinal(String text, int value) {
		super(null, text);
		this.value = value;
	}

	public Cardinal(TokenContext context, String text, int value) {
		super(context, text);
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
	protected void writeName(StringBuilder text) {
		text.append("cardinal");
	}
}