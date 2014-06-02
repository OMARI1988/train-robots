/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.Iterator;

import com.trainrobots.NotImplementedException;
import com.trainrobots.collections.Items;

public abstract class Losr implements Items<Losr> {

	@Override
	public boolean equals(Object object) {
		return object instanceof Losr && equals((Losr) object);
	}

	public abstract boolean equals(Losr losr);

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		write(text);
		return text.toString();
	}

	public void write(StringBuilder text) {

		// Name.
		text.append('(');
		writeName(text);
		text.append(':');

		// Children.
		int size = count();
		for (int i = 0; i < size; i++) {
			text.append(' ');
			get(i).write(text);
		}
		text.append(')');
	}

	protected abstract void writeName(StringBuilder text);

	@Override
	public abstract int count();

	@Override
	public abstract Losr get(int index);

	@Override
	public Losr[] toArray() {
		throw new NotImplementedException();
	}

	@Override
	public Iterator<Losr> iterator() {
		throw new NotImplementedException();
	}

	public static Losr parse(String text) {
		throw new NotImplementedException();
	}
}