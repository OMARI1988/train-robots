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

	public Token(String text) {
		this.text = text;
	}

	public String text() {
		return text;
	}
}