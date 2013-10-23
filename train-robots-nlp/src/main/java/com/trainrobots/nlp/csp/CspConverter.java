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

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Indicator;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Relation;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.csp.constraints.ColorConstraint;
import com.trainrobots.nlp.csp.constraints.IndicatorConstraint;
import com.trainrobots.nlp.csp.constraints.PostIndicatorConstraint;
import com.trainrobots.nlp.csp.constraints.RelationConstraint;
import com.trainrobots.nlp.csp.constraints.TypeConstraint;

public class CspConverter {

	public static EntityNode convert(Rcl rcl, Entity entity) {

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
					EntityNode n2 = convert(rcl, relation.entity());
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
}