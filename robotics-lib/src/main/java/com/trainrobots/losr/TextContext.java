/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.Objects;

public class TextContext {

	private final String text;
	private final int start;
	private final int end;

	public TextContext(int token) {
		this(token, token);
	}

	public TextContext(String text, int token) {
		this.text = text;
		this.start = token;
		this.end = token;
	}

	public TextContext(int start, int end) {
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
	public boolean equals(Object object) {
		if (object instanceof TextContext) {
			TextContext context = (TextContext) object;
			return Objects.equals(context.text, text) && context.start == start
					&& context.end == end;
		}
		return false;
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