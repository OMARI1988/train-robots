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
import com.trainrobots.instructions.Instruction;
import com.trainrobots.instructions.TakeInstruction;
import com.trainrobots.losr.Actions;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Types;
import com.trainrobots.observables.Observable;
import com.trainrobots.observables.distributions.AttributeDistribution;
import com.trainrobots.observables.distributions.ObservableDistribution;
import com.trainrobots.observables.distributions.PickableDistribution;
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

		// (entity: (color: X) (type: Y))
		Entity entity = event.entity();
		if (entity.cardinal() != null) {
			throw new RoboticException("Cardinality is not supported.");
		}
		if (entity.indicators() != null) {
			throw new RoboticException("Indiciators are not supported.");
		}
		if (entity.spatialRelation() != null) {
			throw new RoboticException("Spatial relations are not supported.");
		}
		Types type = entity.type();
		Items<Color> colors = entity.colors();
		if (colors == null || colors.count() != 1) {
			throw new RoboticException("Expected a single color.");
		}

		// Match type and color.
		ObservableDistribution distribution = new AttributeDistribution(layout,
				type, colors.get(0).color());

		// Pickable.
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
}