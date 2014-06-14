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
import com.trainrobots.distributions.observable.ActiveDistribution;
import com.trainrobots.distributions.observable.BackDistribution;
import com.trainrobots.distributions.observable.BetweenObservableDistribution;
import com.trainrobots.distributions.observable.ColorDistribution;
import com.trainrobots.distributions.observable.DroppableDistribution;
import com.trainrobots.distributions.observable.FrontDistribution;
import com.trainrobots.distributions.observable.IndividualDistribution;
import com.trainrobots.distributions.observable.LeftDistribution;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.distributions.observable.PickableDistribution;
import com.trainrobots.distributions.observable.RelativeDistribution;
import com.trainrobots.distributions.observable.ExactIndicatorDistribution;
import com.trainrobots.distributions.observable.RightDistribution;
import com.trainrobots.distributions.observable.TypeDistribution;
import com.trainrobots.distributions.spatial.DestinationDistribution;
import com.trainrobots.distributions.spatial.DropDestinationDistribution;
import com.trainrobots.distributions.spatial.MeasureDistribution;
import com.trainrobots.distributions.spatial.SpatialDistribution;
import com.trainrobots.instructions.DropInstruction;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.instructions.MoveInstruction;
import com.trainrobots.instructions.TakeInstruction;
import com.trainrobots.losr.Actions;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Destination;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Indicators;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Measure;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.Sequence;
import com.trainrobots.losr.Source;
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
		return translateItem(context(losr), losr);
	}

	public ObservableDistribution ground(Losr root, Entity entity) {
		PlannerContext context = context(root);
		return distributionOfEntity(context, entity);
	}

	public DestinationDistribution ground(Losr root, Destination destination) {
		PlannerContext context = context(root);
		return distributionOfDestination(context, destination).destinations(
				context);
	}

	public DestinationDistribution ground(Losr root,
			SpatialRelation spatialRelation) {
		PlannerContext context = context(root);
		return distributionOfSpatialRelation(context, spatialRelation)
				.destinations(context);
	}

	private Instruction translateItem(PlannerContext context, Losr losr) {

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
			instructions[i] = translateItem(context, item);
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
		Entity entity = normalizeSource(event.entity(), event.source());
		ObservableDistribution distribution = distributionOfEntity(context,
				entity);
		distribution = new PickableDistribution(distribution);

		// Single observable.
		Items<Observable> best = distribution.best();
		if (best.count() != 1) {
			throw new RoboticException(
					"Expected a single observable for a move action (%s).",
					count(best.count()));
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
				distributionOfDestination(context, event.destination()));
		Items<Position> destinations = spatialDistribution
				.destinations(context).best();
		if (destinations.count() != 1) {
			throw new RoboticException(
					"Expected a single destination for a move action (%s).",
					count(destinations.count()));
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
		Entity entity = normalizeSource(event.entity(), event.source());
		ObservableDistribution distribution = distributionOfEntity(context,
				entity);
		distribution = new PickableDistribution(distribution);

		// Single observable.
		Items<Observable> best = distribution.best();
		if (best.count() != 1) {
			throw new RoboticException(
					"Expected a single observable for a take action (%s).",
					count(best.count()));
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

		// Source.
		if (event.source() != null) {
			throw new RoboticException(
					"A source should not be specified for a drop action.");
		}

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

		// Match source shape?
		if (!dropEntityReference
				&& entityMatchesShape(entity, context.sourceShape())) {
			dropEntityReference = true;
		}

		// Source.
		Gripper gripper = layout.gripper();
		if (!dropEntityReference) {

			// Droppable.
			ObservableDistribution distribution = distributionOfEntity(context,
					entity);
			distribution = new DroppableDistribution(distribution);

			// Single observable.
			Items<Observable> best = distribution.best();
			if (best.count() != 1) {
				throw new RoboticException(
						"Expected a single observable for a drop action (%s).",
						count(best.count()));
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
		Position position;
		if (event.destination() != null) {
			SpatialDistribution spatialDistribution = new DropDestinationDistribution(
					distributionOfDestination(context, event.destination()));
			Items<Position> destinations = spatialDistribution.destinations(
					context).best();
			if (destinations.count() != 1) {
				throw new RoboticException(
						"Expected a single destination for a drop action (%s).",
						count(destinations.count()));
			}
			position = destinations.get(0);
		} else {
			position = simulator.dropPosition(gripper.position());
		}
		return new DropInstruction(position);
	}

	private static boolean entityMatchesShape(Entity entity, Shape shape) {

		// No shape?
		if (shape == null) {
			return false;
		}

		// Attributes.
		if (entity.cardinal() != null || entity.indicators() != null
				|| entity.spatialRelation() != null) {
			return false;
		}

		// Type.
		if (entity.type() != shape.type()) {
			return false;
		}

		// Colors.
		Items<Colors> colors = entity.colors();
		return colors == null
				|| (colors.count() == 1 && colors.get(0) == shape.color());
	}

	private SpatialDistribution distributionOfDestination(
			PlannerContext context, Destination destination) {

		// Normalize.
		SpatialRelation spatialRelation = destination.spatialRelation();
		if (destination.entity() != null) {
			spatialRelation = new SpatialRelation(
					new Relation(Relations.Above), destination.entity());
		}

		// Distribution.
		return distributionOfSpatialRelation(context, spatialRelation);
	}

	private ObservableDistribution distributionOfEntity(PlannerContext context,
			Entity entity) {

		// Cardinality.
		boolean betweenEntity = context.betweenEntity() == entity;
		if (entity.cardinal() != null) {
			if (entity.cardinal().value() != 1) {

				// between ... two ...
				if (betweenEntity && entity.cardinal().value() == 2) {
				} else {
					throw new RoboticException(
							"Non-singular cardinality is not supported: %s.",
							entity.cardinal());
				}
			}
		}

		// Reference?
		ObservableDistribution distribution = null;
		Types type = entity.type();
		if (type == Types.TypeReference || type == Types.TypeReferenceGroup) {
			type = context.referenceType(type, entity.referenceId());
		}
		if (type == Types.Reference) {
			distribution = context.get(entity.referenceId());
			if (distribution == null) {
				throw new RoboticException(
						"Failed to find distribution for %s.", entity);
			}
			return distribution;
		}

		// Group?
		if (type == Types.CubeGroup || type == Types.PrismGroup) {
			if (betweenEntity) {
				distribution = distributionOfBetweenEntity(type.single(),
						entity.colors());
			} else if (type == Types.CubeGroup) {
				type = Types.Stack;
			}
		}
		if (distribution == null) {
			distribution = new TypeDistribution(context, type);
		}

		// Indicators.
		Items<Indicator> indicators = entity.indicators();
		if (indicators != null) {
			distribution = distributionOfIndicators(context, type, indicators,
					distribution);
		}

		// Colors.
		if (!betweenEntity) {
			Items<Colors> colors = entity.colors();
			if (colors != null) {
				distribution = new ColorDistribution(distribution, colors);
			}
		}

		// Spatial relation.
		if (entity.spatialRelation() != null) {
			SpatialDistribution spatialDistribution = distributionOfSpatialRelation(
					context, entity.spatialRelation());
			distribution = new RelativeDistribution(distribution,
					spatialDistribution);
		}
		context.add(entity, distribution);
		return distribution;
	}

	private ObservableDistribution distributionOfBetweenEntity(Types type,
			Items<Colors> colors) {

		// Colors.
		if (colors == null) {
			throw new RoboticException("Colors not specified.");
		}
		if (colors.count() > 2) {
			throw new RoboticException("More than two colors specified.");
		}
		Colors color1 = colors.get(0);
		Colors color2 = colors.count() == 2 ? colors.get(1) : color1;

		// Distribution.
		return new BetweenObservableDistribution(layout, type, color1, color2);
	}

	private ObservableDistribution distributionOfIndicators(
			PlannerContext context, Types type, Items<Indicator> indicators,
			ObservableDistribution distribution) {
		for (Indicator item : indicators) {
			Indicators indicator = item.indicator();

			// Rightmost/leftmost.
			if (indicator == Indicators.Rightmost) {
				indicator = Indicators.Right;
			} else if (indicator == Indicators.Leftmost) {
				indicator = Indicators.Left;
			}

			// Exact.
			if ((type == Types.Edge || type == Types.Corner || type == Types.Region)
					&& (indicator == Indicators.Left
							|| indicator == Indicators.Right
							|| indicator == Indicators.Front
							|| indicator == Indicators.Back || indicator == Indicators.Center)
					|| (type == Types.Position && indicator == Indicators.Active)) {
				distribution = new ExactIndicatorDistribution(distribution,
						indicator);
				continue;
			}

			// Left.
			if (indicator == Indicators.Left) {
				distribution = new LeftDistribution(distribution);
				continue;
			}

			// Right.
			if (indicator == Indicators.Right) {
				distribution = new RightDistribution(distribution);
				continue;
			}

			// Front.
			if (indicator == Indicators.Front) {
				distribution = new FrontDistribution(distribution);
				continue;
			}

			// Front.
			if (indicator == Indicators.Back) {
				distribution = new BackDistribution(distribution);
				continue;
			}

			// Individual.
			if (indicator == Indicators.Individual) {
				distribution = new IndividualDistribution(distribution);
				continue;
			}

			// Active.
			if (indicator == Indicators.Active) {
				distribution = new ActiveDistribution(distribution);
				continue;
			}

			// Nearest.
			if (indicator == Indicators.Nearest) {
				TypeDistribution robot = new TypeDistribution(context,
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

	private SpatialDistribution distributionOfSpatialRelation(
			PlannerContext context, SpatialRelation spatialRelation) {

		// Relation.
		Relations relation = spatialRelation.relation();

		// Entity.
		Entity entity = spatialRelation.entity();
		context.betweenEntity(relation == Relations.Between ? entity : null);
		ObservableDistribution landmarkDistribution = entity != null ? distributionOfEntity(
				context, entity) : null;
		context.betweenEntity(null);

		// Measure.
		Measure measure = spatialRelation.measure();
		if (measure != null) {
			return distributionOfmeasure(measure, relation,
					landmarkDistribution);
		}

		// Distribution.
		if (landmarkDistribution == null) {
			throw new RoboticException(
					"Expected a landmark entity to be specified in spatial relation.");
		}
		return SpatialDistribution.of(relation, landmarkDistribution);
	}

	private SpatialDistribution distributionOfmeasure(Measure measure,
			Relations relation, ObservableDistribution landmarkDistribution) {

		// Colors.
		Entity entity = measure.entity();
		if (entity.colorAttributes() != null) {
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

	private static Entity normalizeSource(Entity entity, Source source) {

		// Source.
		if (source == null) {
			return entity;
		}

		// Spatial relation.
		SpatialRelation spatialRelation = source.spatialRelation();
		if (spatialRelation == null) {
			Entity sourceEntity = source.entity();
			if (sourceEntity == null) {
				throw new RoboticException("Source entity not specified.");
			}
			spatialRelation = new SpatialRelation(
					new Relation(Relations.Above), sourceEntity);
		}

		// Normalized entity.
		return new Entity(entity.id(), entity.referenceId(), entity.cardinal(),
				entity.indicators(), entity.colorAttributes(),
				entity.typeAttribute(), spatialRelation);
	}

	private PlannerContext context(Losr root) {
		PlannerContext context = new PlannerContext(observables, simulator,
				root);
		context.sourceShape(layout.gripper().shape());
		return context;
	}

	private static String count(int count) {
		return count == 1 ? "1 grounding" : count + " groundings";
	}
}