/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class TokenContext {

	private final String text;
	private final int start;
	private final int end;

	public TokenContext(int token) {
		this(token, token);
	}

	public TokenContext(String text, int token) {
		this.text = text;
		this.start = token;
		this.end = token;
	}

	public TokenContext(int start, int end) {
		this.text = null;
		this.start = start;
		this.end = end;
	}

	public String text() {
		return text;
	}

	public int start() {
		return start;
	}

	public int end() {
		return end;
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append("(token: ");
		text.append(start);
		if (start != end) {
			text.append(' ');
			text.append(end);
		}
		text.append(')');
		return text.toString();
	}
}