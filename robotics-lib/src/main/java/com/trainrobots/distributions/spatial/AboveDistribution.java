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
import com.trainrobots.losr.Indicators;
import com.trainrobots.observables.Board;
import com.trainrobots.observables.Corner;
import com.trainrobots.observables.Edge;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Region;
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
		for (Observable landmark : landmarkDistribution) {

			// Shape.
			if (landmark instanceof Shape) {
				Shape landmarkShape = (Shape) landmark;
				if (observable instanceof Shape) {
					Shape shape = (Shape) observable;
					if (landmarkShape.position().add(0, 0, 1)
							.equals(shape.position())) {
						return 1;
					}
					continue;
				}
			}

			// Corner.
			if (landmark instanceof Corner) {
				Corner corner = (Corner) landmark;
				Position p1 = null;
				if (observable instanceof Shape) {
					p1 = ((Shape) observable).position();
				} else if (observable instanceof Stack) {
					p1 = ((Stack) observable).base().position();
				}
				if (p1 != null) {
					Position p2 = corner.position();
					if (p1.x() == p2.x() && p1.y() == p2.y()) {
						return 1;
					}
					continue;
				}
			}

			// Board.
			if (landmark instanceof Board) {
				if (observable instanceof Shape) {
					Shape shape = (Shape) observable;
					if (shape.position().z() == 0) {
						return 1;
					}
					continue;
				}
			}

			// Edge.
			if (landmark instanceof Edge) {
				Position position = null;
				if (observable instanceof Shape) {
					position = ((Shape) observable).position();
				} else if (observable instanceof Stack) {
					position = ((Stack) observable).base().position();
				}
				if (position != null) {
					if (((Edge) landmark).supports(position)) {
						return 1;
					}
					continue;
				}
			}

			// Stack.
			if (landmark instanceof Stack) {
				if (observable instanceof Shape) {
					Shape shape = (Shape) observable;
					Stack stack = (Stack) landmark;
					if (stack.top().position().add(0, 0, 1)
							.equals(shape.position())) {
						return 1;
					}
					continue;
				}
			}

			// Region.
			if (landmark instanceof Region) {
				Region region = (Region) landmark;
				if (region.indicator() == Indicators.Center) {
					Position p1 = null;
					if (observable instanceof Shape) {
						p1 = ((Shape) observable).position();
					} else if (observable instanceof Stack) {
						p1 = ((Stack) observable).base().position();
					}
					if (p1 != null) {
						if ((p1.x() == 3 || p1.x() == 4)
								&& (p1.y() == 3 || p1.y() == 4)) {
							return 1;
						}
						continue;
					}
				}
			}

			// Not supported.
			throw new RoboticException("%s above %s is not supported.",
					observable, landmark);
		}
		return 0;
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

			// Board.
			else if (landmark instanceof Board && context.sourceShape() != null) {
				Shape shape = context.sourceShape();
				destinations.add(new DestinationHypothesis(context.simulator()
						.dropPosition(shape.position()), null));
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