/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Text extends Token {

	public Text(String text) {
		super(text);
	}

	@Override
	public boolean equals(Losr losr) {
		return losr instanceof Text && ((Text) losr).text.equals(text);
	}

	@Override
	public void write(StringBuilder text) {
		text.append("(text: ");
		text.append(this.text);
		text.append(')');
	}
}