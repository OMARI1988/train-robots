/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Types;
import com.trainrobots.scenes.Shape;

public class Stack extends Observable {

	private final List<Shape> shapes = new ArrayList<Shape>();
	private final Set<Colors> colors = new HashSet<Colors>();
	private final boolean includesHead;

	public Stack(boolean includesHead) {
		this.includesHead = includesHead;
	}

	// TODO: USED??
	public boolean includesHead() {
		return includesHead;
	}

	public void add(Shape shape) {
		shapes.add(shape);
		colors.add(shape.color());
	}

	// TODO: USED??
	public Shape top() {
		return shapes.get(shapes.size() - 1);
	}

	// TODO: USED??
	public Shape base() {
		return shapes.get(0);
	}

	public boolean hasColors(Set<Colors> colors) {
		return this.colors.equals(colors);
	}

	@Override
	public Losr toLosr() {
		return new Entity(Types.Stack);
	}

	public Stack excludeHead() {

		// Must have at least 3 shapes.
		int size = shapes.size();
		if (size < 2) {
			return null;
		}

		// Create new stack.
		Stack headlessStack = new Stack(false);
		for (int i = 0; i < size - 1; i++) {
			headlessStack.add(shapes.get(i));
		}
		return headlessStack;
	}
}