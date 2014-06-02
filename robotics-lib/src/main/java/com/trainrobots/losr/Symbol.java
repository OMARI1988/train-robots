/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Symbol extends Terminal {

	private final char value;

	public Symbol(char value) {
		this(null, value);
	}

	public Symbol(TokenContext context, char value) {
		super(context);
		this.value = value;
	}

	public char value() {
		return value;
	}

	@Override
	public boolean equals(Losr losr) {
		return losr instanceof Symbol && ((Symbol) losr).value == value;
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("symbol");
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(value);
	}
}