/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Types;
import com.trainrobots.scenes.Shape;

public class Stack extends Observable {

	private final List<Shape> shapes = new ArrayList<Shape>();
	private final HashMap<Colors, Double> colors = new HashMap<>();
	private final boolean includesHead;

	public Stack(boolean includesHead) {
		this.includesHead = includesHead;
	}

	public void add(Shape shape) {
		shapes.add(shape);
	}

	public Shape top() {
		return shapes.get(shapes.size() - 1);
	}

	public Shape base() {
		return shapes.get(0);
	}

	public double has(Items<Colors> colors) {

		// Too many colors?
		int expectedSize = this.colors.size();
		int matchSize = colors.count();
		if (matchSize > expectedSize) {
			return 0;
		}

		// Calculated matched weight.
		double matchedWeight = 0;
		for (int i = 0; i < matchSize; i++) {
			Double weight = this.colors.get(colors.get(i));
			if (weight == null) {
				return 0;
			}
			matchedWeight += weight;
		}

		// All colors matched?
		if (matchedWeight == 1) {
			return 1;
		}

		// Only support partial matches above a threshold.
		if (matchedWeight < 0.65) {
			return 0;
		}

		// Scale partial matches to give them a lower weighting.
		return 0.1 * matchedWeight;
	}

	@Override
	public Losr toLosr() {
		return new Entity(Types.Stack);
	}

	public Stack excludeHead() {

		// Already headless?
		if (!includesHead) {
			throw new RoboticException("Stack is already headless.");
		}

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
		headlessStack.normalize();
		return headlessStack;
	}

	public void normalize() {

		// Color counts.
		for (Shape shape : shapes) {
			Colors color = shape.color();
			Double count = colors.get(color);
			colors.put(color, count == null ? 1 : count + 1);
		}

		// Normalize.
		// System.out.println("\nSTACK:");
		int sum = shapes.size();
		for (Entry<Colors, Double> e : colors.entrySet()) {
			colors.put(e.getKey(), e.getValue() / sum);
			// System.out.println("    " + e.getKey() + " "
			// + colors.get(e.getKey()));
		}
	}
}