/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions.spatial;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.distributions.hypotheses.DestinationHypothesis;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.observables.Board;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Stack;
import com.trainrobots.planner.PlannerContext;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class AboveDistribution extends SpatialDistribution {

	private final ObservableDistribution landmarkDistribution;

	public AboveDistribution(ObservableDistribution landmarkDistribution) {
		super(landmarkDistribution.layout());
		this.landmarkDistribution = landmarkDistribution;
	}

	@Override
	public double weight(Observable observable) {

		// Shape.
		if (observable instanceof Shape) {
			Shape shape = (Shape) observable;
			for (Observable landmark : landmarkDistribution) {

				// Shape.
				if (landmark instanceof Shape) {
					Shape landmarkShape = (Shape) landmark;
					if (landmarkShape.position().add(0, 0, 1)
							.equals(shape.position())) {
						return 1;
					}
				}

				// Corner.
				else if (landmark instanceof Corner) {
					Corner corner = (Corner) landmark;
					Position p1 = shape.position();
					Position p2 = corner.position();
					if (p1.x() == p2.x() && p1.y() == p2.y()) {
						return 1;
					}
				}

				// Board.
				else if (landmark instanceof Board) {
					if (shape.position().z() == 0) {
						return 1;
					}
				}

				// Edge.
				else if (landmark instanceof Edge) {
					Edge edge = (Edge) landmark;
					switch (edge.indicator()) {
					case Left:
						if (shape.position().y() == 7) {
							return 1;
						}
						break;
					case Right:
						if (shape.position().y() == 0) {
							return 1;
						}
						break;
					case Front:
						if (shape.position().x() == 7) {
							return 1;
						}
						break;
					case Back:
						if (shape.position().x() == 0) {
							return 1;
						}
						break;
					}
				}

				// Stack.
				else if (landmark instanceof Stack) {
					Stack stack = (Stack) landmark;
					if (stack.top().position().add(0, 0, 1)
							.equals(shape.position())) {
						return 1;
					}
				}

				// Not supported.
				else {
					throw new RoboticException(
							"Above is not supported with landmark %s.",
							landmark);
				}
			}
			return 0;
		}

		// Not supported.
		throw new RoboticException("Above is not supported with %s.",
				observable);
	}

	@Override
	public Items<DestinationHypothesis> destinations(PlannerContext context) {
		ItemsList<DestinationHypothesis> destinations = new ItemsList<DestinationHypothesis>();
		for (Observable landmark : landmarkDistribution) {

			// Shape.
			if (landmark instanceof Shape) {
				Shape landmarkShape = (Shape) landmark;
				destinations.add(new DestinationHypothesis(landmarkShape
						.position().add(0, 0, 1), landmarkShape));
			}

			// Corner.
			else if (landmark instanceof Corner) {
				Corner corner = (Corner) landmark;
				destinations.add(new DestinationHypothesis(context.simulator()
						.dropPosition(corner.position()), corner));
			}

			// Stack.
			else if (landmark instanceof Stack) {
				Stack stack = (Stack) landmark;
				destinations.add(new DestinationHypothesis(stack.top()
						.position().add(0, 0, 1), stack));
			}

			// Not supported.
			else {
				throw new RoboticException(
						"Above destination is not supported with landmark %s.",
						landmark);
			}
		}
		return destinations;
	}
}