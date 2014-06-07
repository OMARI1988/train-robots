/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.losr;

import com.trainrobots.RoboticException;

public class Token {

	private final int id;
	private final String text;

	public Token(int id, String text) {
		if (id == 0) {
			throw new RoboticException("Invalid token ID.");
		}
		this.id = id;
		this.text = text;
	}

	public int id() {
		return id;
	}

	public String text() {
		return text;
	}

	@Override
	public String toString() {
		return id + ":" + text;
	}
}