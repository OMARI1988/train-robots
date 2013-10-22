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
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.csp.constraints.ColorConstraint;
import com.trainrobots.nlp.csp.constraints.IndicatorConstraint;
import com.trainrobots.nlp.csp.constraints.TypeConstraint;

public class CspConverter {

	public static Csp convertRcl(Rcl rcl) {

		// Entity.
		if (rcl instanceof Entity) {
			return convertEntity((Entity) rcl);
		}

		// No match.
		throw new CoreException("Can't convert RCL to CSP.");
	}

	private static Csp convertEntity(Entity entity) {

		// CSP.
		Csp csp = new Csp();

		// Type.
		Type type = entity.typeAttribute().type();
		if (type == null) {
			throw new CoreException("Entity type not specified: " + entity);
		}
		CspVariable variable = csp.add();
		variable.add(new TypeConstraint(type));

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
			variable.add(constraint);
		}

		// Indicators.
		if (entity.indicatorAttributes() != null) {
			for (IndicatorAttribute indicatorAttribute : entity
					.indicatorAttributes()) {
				variable.add(new IndicatorConstraint(indicatorAttribute
						.indicator()));
			}
		}

		// Result.
		return csp;
	}
}