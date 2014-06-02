/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Text extends Terminal {

	protected final String text;

	public Text(String text) {
		this(null, text);
	}

	public Text(TokenContext context, String text) {
		super(context);
		this.text = text;
	}

	public String text() {
		return text;
	}

	@Override
	public boolean equals(Losr losr) {
		return losr instanceof Text && ((Text) losr).text.equals(text);
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("text");
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(this.text);
	}
}