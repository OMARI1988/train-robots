/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.losr.Colors;
import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Shape;

public class ColorDistribution extends ObservableDistribution {

	public ColorDistribution(ObservableDistribution distribution, Colors color) {
		super(distribution.layout());
		for (Observable observable : distribution) {
			if (observable instanceof Shape) {
				Shape shape = (Shape) observable;
				if (shape.color() == color) {
					add(shape);
				}
			}
		}
	}
}