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

	protected final int id;
	protected final int referenceId;

	protected Losr() {
		this(0, 0);
	}

	protected Losr(int id, int referenceId) {
		this.id = id;
		this.referenceId = referenceId;
	}

	public int id() {
		return id;
	}

	public int referenceId() {
		return referenceId;
	}

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

		// ID.
		if (id != 0) {
			text.append(" (id: ");
			text.append(id);
			text.append(')');
		}

		// Children.
		int size = count();
		for (int i = 0; i < size; i++) {
			text.append(' ');
			Losr child = get(i);
			child.write(text);

			// Reference ID.
			if (child instanceof Type && referenceId != 0) {
				text.append(" (reference-id: ");
				text.append(referenceId);
				text.append(')');
			}
		}
		text.append(')');
	}
}