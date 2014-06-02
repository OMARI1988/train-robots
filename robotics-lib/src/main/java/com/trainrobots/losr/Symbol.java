/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Symbol extends Token {

	public Symbol(String text) {
		super(null, text);
	}

	public Symbol(TokenContext context, String text) {
		super(context, text);
	}

	@Override
	public boolean equals(Losr losr) {
		return losr instanceof Symbol && ((Symbol) losr).text.equals(text);
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("symbol");
	}
}