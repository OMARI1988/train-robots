/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.observable;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Types;
import com.trainrobots.observables.Board;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Region;
import com.trainrobots.observables.Robot;
import com.trainrobots.observables.Stack;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Shape;

public class TypeDistribution extends ObservableDistribution {

	public TypeDistribution(PlannerContext context, Layout layout, Types type) {
		super(layout);

		// Shape.
		if (type == Types.Cube || type == Types.Prism) {
			for (Shape shape : layout.shapes()) {
				if (shape.type() == type) {
					add(shape, 1);
				}
			}
			return;
		}

		// Edge.
		if (type == Types.Edge) {
			add(Edge.Left, 1);
			add(Edge.Right, 1);
			add(Edge.Front, 1);
			add(Edge.Back, 1);
			return;
		}

		// Region.
		if (type == Types.Region) {
			add(Region.Left, 1);
			add(Region.Right, 1);
			add(Region.Front, 1);
			add(Region.Back, 1);
			add(Region.Center, 1);
			return;
		}

		// Corner.
		if (type == Types.Corner) {
			add(Corner.FrontLeft, 1);
			add(Corner.FrontRight, 1);
			add(Corner.BackLeft, 1);
			add(Corner.BackRight, 1);
			return;
		}

		// Board.
		if (type == Types.Board) {
			add(Board.board(), 1);
			return;
		}

		// Robot.
		if (type == Types.Robot) {
			add(Robot.robot(), 1);
			return;
		}

		// Stack.
		if (type == Types.Stack || type == Types.CubeGroup) {
			for (Stack stack : context.observables().stacks()) {
				add(stack, 1);
			}
			return;
		}

		// Not supported.
		throw new RoboticException("The type '%s' is not supported.", type);
	}
}