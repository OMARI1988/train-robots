/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.Iterator;
import java.util.function.Consumer;

import com.trainrobots.collections.Items;

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

	public abstract String name();

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

	public TextContext span() {
		int size = count();
		if (size == 0) {
			return null;
		}
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < size; i++) {
			TextContext child = get(i).span();
			if (child == null) {
				continue;
			}
			if (child.start() < min) {
				min = child.start();
			}
			if (child.end() > max) {
				max = child.end();
			}
		}
		return new TextContext(min, max);
	}

	public static Losr parse(String text) {
		return new LosrParser(text).parse();
	}

	public void visit(Consumer<Losr> visitor) {
		visitor.accept(this);
		int size = count();
		for (int i = 0; i < size; i++) {
			get(i).visit(visitor);
		}
	}

	public Losr find(int id) {

		// Match.
		if (id == this.id) {
			return this;
		}

		// Recurse.
		int size = count();
		for (int i = 0; i < size; i++) {
			Losr result = get(i).find(id);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public Path path(Losr item) {
		return path(null, item);
	}

	protected void write(StringBuilder text) {

		// Name.
		text.append('(');
		text.append(name());
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

	private Path path(Path parentPath, Losr item) {

		// Use reference equality to void duplicates.
		Path path = parentPath != null ? parentPath.add(this) : new Path(this);
		if (this == item) {
			return path;
		}

		// Recurse.
		int size = count();
		for (int i = 0; i < size; i++) {
			Path result = get(i).path(path, item);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
}