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
import com.trainrobots.distributions.ColorDistribution;
import com.trainrobots.distributions.IndicatorDistribution;
import com.trainrobots.distributions.TypeDistribution;
import com.trainrobots.distributions.ObservableDistribution;
import com.trainrobots.distributions.PickableDistribution;
import com.trainrobots.distributions.RelationDistribution;
import com.trainrobots.distributions.RelativeDistribution;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.instructions.TakeInstruction;
import com.trainrobots.losr.Actions;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Types;
import com.trainrobots.observables.Observable;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Shape;

public class Planner {

	private final Layout layout;

	public Planner(Layout layout) {
		this.layout = layout;
	}

	public Instruction translate(Losr losr) {

		// (event: (action: take) (entity: X))
		if (!(losr instanceof Event)) {
			throw new RoboticException("Expected an event.");
		}
		Event event = (Event) losr;
		if (event.action() != Actions.Take) {
			throw new RoboticException("Expected a take action.");
		}
		if (event.destination() != null) {
			throw new RoboticException(
					"A destination should not be specified for a take action.");
		}

		// Pickable.
		ObservableDistribution distribution = distribution(event.entity());
		distribution = new PickableDistribution(distribution);

		// Single observable.
		if (distribution.count() != 1) {
			throw new RoboticException("Expected a single observable.");
		}
		Observable observable = distribution.get(0);

		// Shape.
		if (!(observable instanceof Shape)) {
			throw new RoboticException("Observable was not a shape.");
		}
		Shape shape = (Shape) observable;
		return new TakeInstruction(shape.position());
	}

	private ObservableDistribution distribution(Entity entity) {

		// Cardinality.
		if (entity.cardinal() != null) {
			throw new RoboticException("Cardinality is not supported.");
		}

		// Type.
		Types type = entity.type();
		ObservableDistribution distribution = new TypeDistribution(layout, type);

		// Color.
		Items<Color> colors = entity.colors();
		if (colors != null) {
			if (colors.count() != 1) {
				throw new RoboticException("Expected at most a single color.");
			}
			distribution = new ColorDistribution(distribution, colors.get(0)
					.color());
		}

		// Indicator.
		Items<Indicator> indicators = entity.indicators();
		if (indicators != null) {
			distribution = new IndicatorDistribution(distribution, indicators);
		}

		// Spatial relation.
		if (entity.spatialRelation() != null) {
			RelationDistribution relationDistribution = distribution(entity
					.spatialRelation());
			distribution = new RelativeDistribution(distribution,
					relationDistribution);
		}
		return distribution;
	}

	private RelationDistribution distribution(SpatialRelation spatialRelation) {

		// (spatial-relation: (relation: X) (entity: Y))
		if (spatialRelation.measure() != null) {
			throw new RoboticException("Measures are not supported.");
		}
		if (spatialRelation.entity() == null) {
			throw new RoboticException(
					"Spatial relations without entities are not supported.");
		}
		Relations relation = spatialRelation.relation();
		Entity entity = spatialRelation.entity();

		// Entity.
		ObservableDistribution landmarkDistribution = distribution(entity);
		return new RelationDistribution(relation, landmarkDistribution);
	}
}