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

	public Symbol(TextContext context, char value) {
		super(context);
		this.value = value;
	}

	public char value() {
		return value;
	}

	@Override
	public String name() {
		return "symbol";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Symbol) {
			Symbol symbol = (Symbol) losr;
			return symbol.id == id && symbol.referenceId == referenceId
					&& symbol.value == value;
		}
		return false;
	}

	@Override
	public Symbol withContext(TextContext context) {
		return new Symbol(context, value);
	}

	@Override
	protected Object content() {
		return value;
	}
}