/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import java.util.HashSet;
import java.util.Set;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Colors;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Stack;
import com.trainrobots.scenes.Shape;

public class ColorDistribution extends ObservableDistribution {

	public ColorDistribution(ObservableDistribution distribution,
			Items<Color> colors) {
		super(distribution.layout());
		for (Observable observable : distribution) {

			// Shape.
			if (observable instanceof Shape) {
				Shape shape = (Shape) observable;
				if (colors.count() == 1
						&& shape.color() == colors.get(0).color()) {
					add(shape);
				}
			}

			// Stack.
			else if (observable instanceof Stack) {
				Stack stack = (Stack) observable;
				Set<Colors> set = new HashSet<Colors>();
				for (Color color : colors) {
					set.add(color.color());
				}
				if (stack.hasColors(set)) {
					add(stack);
				}
			}
		}
	}
}