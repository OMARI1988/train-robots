/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public abstract class Terminal extends Losr {

	protected final TextContext context;

	protected Terminal(TextContext context) {
		this.context = context;
	}

	public TextContext context() {
		return context;
	}

	@Override
	public TextContext span() {
		return context;
	}

	@Override
	public int count() {
		return 0;
	}

	@Override
	public Losr get(int index) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	protected void write(StringBuilder text) {

		// Name.
		text.append('(');
		text.append(name());
		text.append(": ");

		// Content.
		writeContent(text);

		// Context.
		if (context != null) {
			text.append(' ');
			text.append(context);
		}
		text.append(')');
	}

	protected abstract void writeContent(StringBuilder text);
}