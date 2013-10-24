/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.nlp.csp;

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Indicator;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Relation;
import com.trainrobots.core.rcl.RelationAttribute;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.csp.constraints.ColorConstraint;
import com.trainrobots.nlp.csp.constraints.DestinationConstraint;
import com.trainrobots.nlp.csp.constraints.DestinationWithMeasureConstraint;
import com.trainrobots.nlp.csp.constraints.IndicatorConstraint;
import com.trainrobots.nlp.csp.constraints.PositionConstraint;
import com.trainrobots.nlp.csp.constraints.PostIndicatorConstraint;
import com.trainrobots.nlp.csp.constraints.RelationConstraint;
import com.trainrobots.nlp.csp.constraints.TypeConstraint;

public class Csp {

	private Csp() {
	}

	public static ActionNode fromAction(Rcl rcl, Rcl element) {
		if (element instanceof Event) {
			return fromEvent(rcl, (Event) element);
		}
		if (element instanceof Sequence) {
			return fromSequence(rcl, (Sequence) element);
		}
		throw new CoreException("Failed to convert RCL to CSP.");
	}

	public static ActionNode fromSequence(Rcl rcl, Sequence sequence) {
		EventNode eventNode = matchRecognizedSequence(sequence);
		if (eventNode != null) {
			return eventNode;
		}
		SequenceNode sequenceNode = new SequenceNode();
		for (Event event : sequence.events()) {
			sequenceNode.add(fromEvent(rcl, event));
		}
		return sequenceNode;
	}

	public static EventNode fromEvent(Rcl rcl, Event event) {

		// Action.
		Action action = event.actionAttribute().action();

		// Entity.
		if (event.entity() == null) {
			throw new CoreException("Event entity not specified.");
		}
		EntityNode entity = fromEntity(rcl, event.entity());

		// Destination.
		PositionConstraint destination = null;
		if (event.destinations() != null && event.destinations().size() == 1) {
			destination = createDestinationConstraint(rcl, event.destinations()
					.get(0));
		}
		return new EventNode(action, entity, destination);
	}

	public static EntityNode fromEntity(Rcl rcl, Entity entity) {

		// Type.
		Type type = entity.typeAttribute().type();
		if (type == null) {
			throw new CoreException("Entity type not specified: " + entity);
		}

		// Type reference?
		if (type == Type.typeReference || type == Type.typeReferenceGroup) {
			if (entity.referenceId() == null) {
				throw new CoreException("Reference ID not specified: " + entity);
			}
			Entity antecedent = (Entity) rcl.getElement(entity.referenceId());
			if (antecedent == null) {
				throw new CoreException("Failed to resolve reference: "
						+ entity);
			}
			if (type == Type.typeReferenceGroup) {
				type = makeGroup(antecedent.typeAttribute().type());
			} else {
				type = antecedent.typeAttribute().type();
			}
		}

		// Reference ID.
		else if (entity.referenceId() != null) {
			throw new CoreException("Unexpected reference ID: "
					+ entity.referenceId());
		}

		// Cube group?
		if (type == Type.cubeGroup) {
			type = Type.stack;
		}

		// Entity node.
		EntityNode n = new EntityNode();
		n.add(new TypeConstraint(type));

		// Ordinal.
		if (entity.ordinalAttribute() != null) {
			throw new CoreException("Unexpected ordinal: "
					+ entity.ordinalAttribute());
		}

		// Cardinal.
		if (entity.cardinalAttribute() != null
				&& entity.cardinalAttribute().cardinal() != 1) {
			throw new CoreException("Unexpected cardinal: "
					+ entity.cardinalAttribute());
		}

		// Colors.
		if (entity.colorAttributes() != null
				&& entity.colorAttributes().size() >= 1) {
			ColorConstraint constraint = new ColorConstraint();
			for (ColorAttribute attribute : entity.colorAttributes()) {
				constraint.add(attribute.color());
			}
			n.add(constraint);
		}

		// Indicators.
		Indicator postIndicator = null;
		if (entity.indicatorAttributes() != null) {
			for (IndicatorAttribute indicatorAttribute : entity
					.indicatorAttributes()) {
				Indicator indicator = indicatorAttribute.indicator();
				if ((type == Type.cube || type == Type.prism || type == Type.stack)
						&& (indicator == Indicator.left
								|| indicator == Indicator.leftmost
								|| indicator == Indicator.right
								|| indicator == Indicator.rightmost || indicator == Indicator.nearest)) {
					if (postIndicator != null) {
						throw new CoreException("Duplicate post indicator in "
								+ entity);
					}
					postIndicator = indicator;
				} else {
					n.add(new IndicatorConstraint(indicator));
				}
			}
		}

		// Relations.
		if (entity.relations() != null) {
			for (SpatialRelation relation : entity.relations()) {

				// Measure?
				if (relation.measure() != null && relation.entity() != null) {
					throw new CoreException(
							"Failed to create predicate for measure: "
									+ relation.measure());
				}

				// Region.
				if (relation.entity() != null
						&& relation.entity().isType(Type.region)) {
					if (relation.entity().indicatorAttributes().size() == 1) {
						Relation r = relation.relationAttribute().relation();
						if (r == Relation.above || r == Relation.within) {
							if (postIndicator != null) {
								throw new CoreException(
										"Duplicate post indicator in " + entity);
							}
							postIndicator = relation.entity()
									.indicatorAttributes().get(0).indicator();
							continue;
						}
					}
				}

				// Constraint.
				if (relation.entity() != null) {
					EntityNode n2 = fromEntity(rcl, relation.entity());
					n.add(new RelationConstraint(relation.relationAttribute()
							.relation(), n2));
				}
			}
		}

		// Post-indicator.
		if (postIndicator != null) {
			n.add(new PostIndicatorConstraint(postIndicator));
		}

		// Result.
		return n;
	}

	private static Type makeGroup(Type type) {
		if (type == Type.cube) {
			return Type.cubeGroup;
		}
		throw new CoreException("Failed to determine group type for '" + type
				+ "'.");
	}

	private static PositionConstraint createDestinationConstraint(Rcl rcl,
			SpatialRelation relation) {

		// Relation.
		RelationAttribute relationAttribute = relation.relationAttribute();
		if (relationAttribute == null) {
			throw new CoreException("Relation not specified.");
		}

		// Measure?
		Entity measure = relation.measure();
		if (measure != null) {
			EntityNode entity = null;
			if (relation.entity() != null) {
				entity = Csp.fromEntity(rcl, relation.entity());
			}
			if (measure.cardinalAttribute() == null) {
				throw new CoreException("Cardinal not specified: " + relation);
			}
			int cardinal = measure.cardinalAttribute().cardinal();
			return new DestinationWithMeasureConstraint(measure.typeAttribute()
					.type(), cardinal, relationAttribute.relation(), entity);
		}

		// Default.
		if (relation.entity() == null) {
			throw new CoreException("Spatial relation entity not specified: "
					+ relation);
		}
		return new DestinationConstraint(relationAttribute.relation(),
				Csp.fromEntity(rcl, relation.entity()));
	}

	private static EventNode matchRecognizedSequence(Sequence sequence) {

		// Events.
		List<Event> events = sequence.events();
		if (events.size() != 2) {
			return null;
		}

		// Take.
		Event event1 = events.get(0);
		if (!event1.isAction(Action.take) || event1.destinations() == null
				|| event1.destinations().size() > 0) {
			return null;
		}
		Entity entity1 = event1.entity();
		Integer id = entity1.id();
		if (id == null) {
			return null;
		}

		// Drop.
		Event event2 = events.get(1);
		if (!event2.isAction(Action.drop)) {
			return null;
		}
		Entity entity2 = event2.entity();
		if (!entity2.isType(Type.reference) || entity2.referenceId() == null
				|| !entity2.referenceId().equals(id)) {
			return null;
		}

		// Validate.
		if (entity2.relations() != null && entity2.relations().size() >= 1) {
			throw new CoreException("References must not have relations: "
					+ entity2);
		}

		// Translate equivalent move.
		Event event3 = new Event(new ActionAttribute(Action.move), entity1,
				event2.destinations());
		return fromEvent(event3, event3);
	}
}