/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.planner;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.distributions.observable.ColorDistribution;
import com.trainrobots.distributions.observable.DroppableDistribution;
import com.trainrobots.distributions.observable.IndividualDistribution;
import com.trainrobots.distributions.observable.LeftDistribution;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.distributions.observable.PickableDistribution;
import com.trainrobots.distributions.observable.RelativeDistribution;
import com.trainrobots.distributions.observable.ExactIndicatorDistribution;
import com.trainrobots.distributions.observable.RightDistribution;
import com.trainrobots.distributions.observable.TypeDistribution;
import com.trainrobots.distributions.spatial.DropDestinationDistribution;
import com.trainrobots.distributions.spatial.MeasureDistribution;
import com.trainrobots.distributions.spatial.SpatialDistribution;
import com.trainrobots.instructions.DropInstruction;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.instructions.MoveInstruction;
import com.trainrobots.instructions.TakeInstruction;
import com.trainrobots.losr.Actions;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Indicators;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Measure;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.Sequence;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Types;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.Observables;
import com.trainrobots.scenes.Gripper;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;
import com.trainrobots.simulator.Simulator;

public class Planner {

	private final Layout layout;
	private final Observables observables;
	private final Simulator simulator;

	public Planner(Layout layout) {
		this(new Simulator(layout));
	}

	public Planner(Simulator simulator) {
		this.layout = simulator.layout();
		this.observables = new Observables(layout);
		this.simulator = simulator;
	}

	public Instruction instruction(Losr losr) {
		return translate(context(losr), losr);
	}

	public ObservableDistribution distribution(Entity entity) {
		return distribution(context(entity), entity);
	}

	private Instruction translate(PlannerContext context, Losr losr) {

		// Event.
		if (losr instanceof Event) {
			return translateEvent(context, (Event) losr);
		}

		// Sequence.
		if (losr instanceof Sequence) {
			return translateSequence(context, (Sequence) losr);
		}

		// Not supported.
		throw new RoboticException("Expected an event or sequence.", losr);
	}

	private Instruction translateSequence(PlannerContext context,
			Sequence sequence) {

		// Translate.
		int size = sequence.count();
		Instruction[] instructions = new Instruction[size];
		for (int i = 0; i < size; i++) {
			Losr item = sequence.get(i);
			instructions[i] = translate(context, item);
			context.previousEvent(item instanceof Event ? (Event) item : null);
		}

		// Merge.
		return Instruction.merge(new ItemsArray(instructions));
	}

	private Instruction translateEvent(PlannerContext context, Event event) {

		// Action.
		Actions action = event.action();
		switch (action) {
		case Move:
			return translateMove(context, event);
		case Take:
			return translateTake(context, event);
		case Drop:
			return translateDrop(context, event);
		}

		// Not supported.
		throw new RoboticException("The action '%s' is not supported.", action);
	}

	private MoveInstruction translateMove(PlannerContext context, Event event) {

		// Pickable.
		ObservableDistribution distribution = distribution(context,
				event.entity());
		distribution = new PickableDistribution(distribution);

		// Single observable.
		Items<Observable> best = distribution.best();
		if (best.count() != 1) {
			throw new RoboticException(
					"Expected a single observable for a move action.");
		}
		Observable observable = best.get(0);

		// Shape.
		if (!(observable instanceof Shape)) {
			throw new RoboticException(
					"Observable was not a shape for a move action.");
		}
		Shape shape = (Shape) observable;
		context.sourceShape(shape);

		// Destination.
		if (event.destination() == null) {
			throw new RoboticException(
					"Destination not specified for move action.");
		}
		SpatialDistribution spatialDistribution = new DropDestinationDistribution(
				distribution(context, event.destination()));
		Items<Position> destinations = spatialDistribution
				.destinations(context).best();
		if (destinations.count() != 1) {
			throw new RoboticException(
					"Expected a single destination for a move action.");
		}
		return new MoveInstruction(shape.position(), destinations.get(0));
	}

	private TakeInstruction translateTake(PlannerContext context, Event event) {

		// Destination.
		if (event.destination() != null) {
			throw new RoboticException(
					"A destination should not be specified for a take action.");
		}

		// Pickable.
		ObservableDistribution distribution = distribution(context,
				event.entity());
		distribution = new PickableDistribution(distribution);

		// Single observable.
		Items<Observable> best = distribution.best();
		if (best.count() != 1) {
			throw new RoboticException(
					"Expected a single observable for a take action.");
		}
		Observable observable = best.get(0);

		// Shape.
		if (!(observable instanceof Shape)) {
			throw new RoboticException(
					"Observable was not a shape for a take action.");
		}
		Shape shape = (Shape) observable;
		context.sourceShape(shape);
		return new TakeInstruction(shape.position());
	}

	private DropInstruction translateDrop(PlannerContext context, Event event) {

		// Entity reference?
		Entity entity = event.entity();
		Types type = entity.type();
		boolean dropEntityReference = false;
		if (type == Types.Reference) {
			if (context.previousEvent() != null) {
				Event previousEvent = (Event) context.previousEvent();
				if (previousEvent.action() == Actions.Take
						&& previousEvent.entity().id() == entity.referenceId()) {
					dropEntityReference = true;
				}
			}
			if (!dropEntityReference) {
				throw new RoboticException(
						"Failed to resolve drop entity reference to previous take action.");
			}
		}

		// Source.
		Gripper gripper = layout.gripper();
		if (!dropEntityReference) {

			// Droppable.
			ObservableDistribution distribution = distribution(context, entity);
			distribution = new DroppableDistribution(distribution);

			// Single observable.
			Items<Observable> best = distribution.best();
			if (best.count() != 1) {
				throw new RoboticException(
						"Expected a single observable for a drop action.");
			}
			Observable observable = best.get(0);

			// Shape.
			if (!(observable instanceof Shape)) {
				throw new RoboticException(
						"Observable was not a shape for a drop action.");
			}
			Shape shape = (Shape) observable;
			if (!shape.equals(gripper.shape())) {
				throw new RoboticException(
						"Specified shape does not match gripper shape for a drop action.");
			}
		}

		// Destination.
		Position position = simulator.dropPosition(gripper.position());
		if (event.destination() != null) {
			SpatialDistribution spatialDistribution = new DropDestinationDistribution(
					distribution(context, event.destination()));
			Items<Position> destinations = spatialDistribution.destinations(
					context).best();
			if (destinations.count() != 1) {
				throw new RoboticException(
						"Expected a single destination for a drop action.");
			}
			position = destinations.get(0);
		}
		return new DropInstruction(position);
	}

	private ObservableDistribution distribution(PlannerContext context,
			Entity entity) {

		// Cardinality.
		if (entity.cardinal() != null) {
			if (entity.cardinal().value() != 1) {
				throw new RoboticException(
						"Non-singular cardinality is not supported: %s.",
						entity.cardinal());
			}
		}

		// Type.
		Types type = entity.type();
		if (type == Types.TypeReference || type == Types.TypeReferenceGroup) {
			type = context.referenceType(entity.referenceId());
		}
		ObservableDistribution distribution = new TypeDistribution(context,
				layout, type);

		// Indicators.
		Items<Indicator> indicators = entity.indicators();
		if (indicators != null) {
			distribution = distribution(context, type, indicators, distribution);
		}

		// Colors.
		Items<Color> colors = entity.colors();
		if (colors != null) {
			distribution = new ColorDistribution(distribution, colors);
		}

		// Spatial relation.
		if (entity.spatialRelation() != null) {
			SpatialDistribution spatialDistribution = distribution(context,
					entity.spatialRelation());
			distribution = new RelativeDistribution(distribution,
					spatialDistribution);
		}
		return distribution;
	}

	private ObservableDistribution distribution(PlannerContext context,
			Types type, Items<Indicator> indicators,
			ObservableDistribution distribution) {
		for (Indicator item : indicators) {
			Indicators indicator = item.indicator();

			// Exact.
			if ((type == Types.Edge || type == Types.Corner || type == Types.Region)
					&& (indicator == Indicators.Left
							|| indicator == Indicators.Right
							|| indicator == Indicators.Front
							|| indicator == Indicators.Back || indicator == Indicators.Center)) {
				distribution = new ExactIndicatorDistribution(distribution,
						indicator);
				continue;
			}

			// Left.
			if (indicator == Indicators.Left
					|| indicator == Indicators.Leftmost) {
				distribution = new LeftDistribution(distribution);
				continue;
			}

			// Right.
			if (indicator == Indicators.Right
					|| indicator == Indicators.Rightmost) {
				distribution = new RightDistribution(distribution);
				continue;
			}

			// Individual.
			if (indicator == Indicators.Individual) {
				distribution = new IndividualDistribution(distribution);
				continue;
			}

			// Nearest.
			if (indicator == Indicators.Nearest) {
				TypeDistribution robot = new TypeDistribution(context, layout,
						Types.Robot);
				SpatialDistribution nearestRobot = SpatialDistribution.of(
						Relations.Nearest, robot);
				distribution = new RelativeDistribution(distribution,
						nearestRobot);
				continue;
			}

			// Not supported.
			throw new RoboticException("The indicator '%s' is not supported.",
					indicator);
		}
		return distribution;
	}

	private SpatialDistribution distribution(PlannerContext context,
			SpatialRelation spatialRelation) {

		// Relation.
		Relations relation = spatialRelation.relation();

		// Entity.
		Entity entity = spatialRelation.entity();
		ObservableDistribution landmarkDistribution = entity != null ? distribution(
				context, entity) : null;

		// Measure.
		Measure measure = spatialRelation.measure();
		if (measure != null) {
			return distribution(measure, relation, landmarkDistribution);
		}

		// Distribution.
		if (landmarkDistribution == null) {
			throw new RoboticException(
					"Expected a landmark entity to be specified in spatial relation.");
		}
		return SpatialDistribution.of(relation, landmarkDistribution);
	}

	private SpatialDistribution distribution(Measure measure,
			Relations relation, ObservableDistribution landmarkDistribution) {

		// Colors.
		Entity entity = measure.entity();
		if (entity.colors() != null) {
			throw new RoboticException(
					"Measure entity colors are not supported.");
		}

		// Indiciators.
		if (entity.indicators() != null) {
			throw new RoboticException(
					"Measure entity indicators are not supported.");
		}

		// Relation.
		if (entity.spatialRelation() != null) {
			throw new RoboticException(
					"Measure entity relations are not supported.");
		}

		// Type.
		if (entity.type() != Types.Tile) {
			throw new RoboticException(
					"The measure entity type '%s' is not supported.",
					entity.type());
		}

		// Cardinality.
		Cardinal cardinal = entity.cardinal();
		if (cardinal == null) {
			throw new RoboticException(
					"Measure entity cardinality was not specified.");
		}
		int tileCount = cardinal.value();

		// Distribution.
		return new MeasureDistribution(layout, tileCount, relation,
				landmarkDistribution);
	}

	private PlannerContext context(Losr root) {
		PlannerContext context = new PlannerContext(observables, simulator,
				root);
		context.sourceShape(layout.gripper().shape());
		return context;
	}
}