/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables.distributions;

import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Types;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Shape;

public class AttributeDistribution extends ObservableDistribution {

	public AttributeDistribution(Layout layout, Types type, Colors color) {
		super(layout);
		for (Shape shape : layout.shapes()) {
			if (shape.type() == type && shape.color() == color) {
				observables.add(shape);
			}
		}
	}
}