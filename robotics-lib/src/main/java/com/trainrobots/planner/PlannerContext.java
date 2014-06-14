/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.planner;

import java.util.HashMap;
import java.util.Map;

import com.trainrobots.RoboticException;
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Types;
import com.trainrobots.observables.Observables;
import com.trainrobots.scenes.Shape;
import com.trainrobots.simulator.Simulator;

public class PlannerContext {

	private final Map<Integer, ObservableDistribution> distributions = new HashMap<>();
	private Observables observables;
	private final Simulator simulator;
	private final Losr root;
	private Event previousEvent;
	private Shape sourceShape;
	private Entity betweenEntity;

	public PlannerContext(Observables observables, Simulator simulator,
			Losr root) {
		this.observables = observables;
		this.simulator = simulator;
		this.root = root;
	}

	public Observables observables() {
		return observables;
	}

	public Simulator simulator() {
		return simulator;
	}

	public Losr reference(int referenceId) {

		// Not specified?
		if (referenceId == 0) {
			throw new RoboticException("Reference ID not specified.");
		}

		// Find.
		Losr result = root.find(referenceId);
		if (result == null) {
			throw new RoboticException(
					"The reference ID %d could not be resolved.", referenceId);
		}
		return result;
	}

	public Types referenceType(Types type, int referenceId) {
		Losr reference = reference(referenceId);
		if (!(reference instanceof Entity)) {
			throw new RoboticException("Expected an entity reference.");
		}
		Types referenceType = ((Entity) reference).type();
		if (type == Types.TypeReferenceGroup) {
			referenceType = referenceType.group();
		}
		return referenceType;
	}

	public Event previousEvent() {
		return previousEvent;
	}

	public void previousEvent(Event previousEvent) {
		this.previousEvent = previousEvent;
	}

	public Shape sourceShape() {
		return sourceShape;
	}

	public void sourceShape(Shape sourceShape) {
		this.sourceShape = sourceShape;
	}

	public Entity betweenEntity() {
		return betweenEntity;
	}

	public void betweenEntity(Entity betweenEntity) {
		this.betweenEntity = betweenEntity;
	}

	public void add(Entity entity, ObservableDistribution distribution) {
		if (entity.id() != 0) {
			distributions.put(entity.id(), distribution);
		}
	}

	public ObservableDistribution get(int id) {
		return distributions.get(id);
	}
}