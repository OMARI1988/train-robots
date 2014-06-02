/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.Iterator;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.reader.LosrReader;

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

	@Override
	public abstract int count();

	@Override
	public abstract Losr get(int index);

	@Override
	public Losr[] toArray() {
		int size = count();
		Losr[] items = new Losr[size];
		for (int i = 0; i < size; i++) {
			items[i] = get(i);
		}
		return items;
	}

	@Override
	public Iterator<Losr> iterator() {
		return new LosrIterator(this);
	}

	public static Losr read(String text) {
		return new LosrReader(text).read();
	}

	protected abstract void writeName(StringBuilder text);

	protected void write(StringBuilder text) {

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
}