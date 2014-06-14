/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Types;
import com.trainrobots.observables.ShapePair;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class BetweenObservableDistribution extends ObservableDistribution {

	public BetweenObservableDistribution(Layout layout, Types type,
			Colors color1, Colors color2) {
		super(layout);

		// Shapes.
		Items<Shape> shapes = layout.shapes();
		int size = shapes.count();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < i; j++) {

				// Types.
				Shape shape1 = shapes.get(i);
				Shape shape2 = shapes.get(j);
				if (shape1.type() != type || shape2.type() != type) {
					continue;
				}

				// Colors.
				if ((shape1.color() == color1 && shape2.color() == color2)
						|| (shape1.color() == color2 && shape2.color() == color1)) {

					// Positions.
					Position p1 = shape1.position();
					Position p2 = shape2.position();
					int dx = p1.x() - p2.x();
					int dy = p1.y() - p2.y();
					int dz = p1.z() - p2.z();
					if (dz == 0
							&& ((dx == 0 && (dy == -2 || dy == 2)) || (dy == 0 && (dx == -2 || dx == 2)))) {
						Shape s1 = layout.shape(p1.add(0, 0, 1));
						Shape s2 = layout.shape(p2.add(0, 0, 1));

						// Clear?
						double weight = s1 == null && s2 == null ? 1 : 0.5;
						add(new ShapePair(shape1, shape2), weight);
					}
				}
			}
		}
		normalize();
	}
}