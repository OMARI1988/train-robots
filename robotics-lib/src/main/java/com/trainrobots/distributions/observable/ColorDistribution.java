/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.collections.SingleItem;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Colors;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Stack;
import com.trainrobots.scenes.Shape;

public class ColorDistribution extends ObservableDistribution {

	public ColorDistribution(ObservableDistribution distribution,
			Items<Color> attributes) {
		super(distribution.layout());
		Items<Colors> colors = colors(attributes);
		for (ObservableHypothesis hypothesis : distribution) {
			Observable observable = hypothesis.observable();
			double weight = hypothesis.weight();

			// Shape.
			if (observable instanceof Shape) {
				Shape shape = (Shape) observable;
				if (colors.count() == 1 && shape.color() == colors.get(0)) {
					add(hypothesis);
				}
				continue;
			}

			// Stack.
			if (observable instanceof Stack) {
				Stack stack = (Stack) observable;
				add(observable, weight * stack.has(colors));
				continue;
			}

			// Not supported.
			throw new RoboticException("Color is not supported with %s.",
					observable);
		}
		normalize();
	}

	private static Items<Colors> colors(Items<Color> attributes) {

		// Single color?
		int size = attributes.count();
		if (size == 1) {
			return new SingleItem(attributes.get(0).color());
		}

		// Get unique colors.
		ItemsList<Colors> colors = new ItemsList<>();
		for (int i = 0; i < size; i++) {
			Colors color = attributes.get(i).color();
			if (!colors.contains(color)) {
				colors.add(color);
			}
		}
		return colors;
	}
}