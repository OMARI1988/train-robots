/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Types;
import com.trainrobots.observables.Board;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Region;
import com.trainrobots.observables.Robot;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Shape;

public class TypeDistribution extends ObservableDistribution {

	public TypeDistribution(Layout layout, Types type) {
		super(layout);

		// Shape.
		if (type == Types.Cube || type == Types.Prism) {
			for (Shape shape : layout.shapes()) {
				if (shape.type() == type) {
					add(shape);
				}
			}
			return;
		}

		// Edge.
		if (type == Types.Edge) {
			add(Edge.Left);
			add(Edge.Right);
			add(Edge.Front);
			add(Edge.Back);
			return;
		}

		// Region.
		if (type == Types.Region) {
			add(Region.Left);
			add(Region.Right);
			add(Region.Front);
			add(Region.Back);
			add(Region.Center);
			return;
		}

		// Corner.
		if (type == Types.Corner) {
			add(Corner.FrontLeft);
			add(Corner.FrontRight);
			add(Corner.BackLeft);
			add(Corner.BackRight);
			return;
		}

		// Board.
		if (type == Types.Board) {
			add(Board.board());
			return;
		}

		// Robot.
		if (type == Types.Robot) {
			add(Robot.robot());
			return;
		}

		// Not supported.
		throw new RoboticException("The type '%s' is not supported.", type);
	}
}