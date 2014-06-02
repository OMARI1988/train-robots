/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public abstract class Token extends Terminal {

	protected final String text;

	protected Token(TokenContext context, String text) {
		super(context);
		this.text = text;
	}

	public String text() {
		return text;
	}

	@Override
	protected void write(StringBuilder text) {
		text.append('(');
		writeName(text);
		text.append(": ");
		text.append(this.text);
		if (context != null) {
			text.append(' ');
			text.append(context);
		}
		text.append(')');
	}
}