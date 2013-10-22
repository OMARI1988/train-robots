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
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.csp.constraints.ColorConstraint;
import com.trainrobots.nlp.csp.constraints.IndicatorConstraint;
import com.trainrobots.nlp.csp.constraints.RelationConstraint;
import com.trainrobots.nlp.csp.constraints.TypeConstraint;

public class CspConverter {

	private final Rcl rcl;
	private final Csp csp;
	private int count;

	public CspConverter(Rcl rcl, Rcl element) {

		// Context.
		this.rcl = rcl;

		// Entity?
		if (!(element instanceof Entity)) {
			throw new CoreException("Can't convert RCL to CSP.");
		}

		// Convert.
		csp = new Csp(convert((Entity) element));
	}

	public Csp csp() {
		return csp;
	}

	private CspVariable convert(Entity entity) {

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

		// Variable.
		CspVariable v = createVariable();
		v.add(new TypeConstraint(type));

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
			v.add(constraint);
		}

		// Indicators.
		if (entity.indicatorAttributes() != null
				&& entity.indicatorAttributes().size() >= 1) {
			for (IndicatorAttribute indicatorAttribute : entity
					.indicatorAttributes()) {
				v.add(new IndicatorConstraint(indicatorAttribute.indicator()));
			}
		}

		// Relations.
		if (entity.relations() != null && entity.relations().size() >= 1) {
			for (SpatialRelation relation : entity.relations()) {
				CspVariable v2 = convert(relation.entity());
				v.add(new RelationConstraint(relation.relationAttribute()
						.relation(), v2));
			}
		}

		// Result.
		return v;
	}

	private CspVariable createVariable() {
		return new CspVariable(++count);
	}

	private static Type makeGroup(Type type) {
		if (type == Type.cube) {
			return Type.cubeGroup;
		}
		throw new CoreException("Failed to determine group type for '" + type
				+ "'.");
	}
}