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

package com.trainrobots.nlp.parser;

import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.CardinalAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.OrdinalAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.core.rcl.RelationAttribute;
import com.trainrobots.core.rcl.TypeAttribute;

public class TokenizedTree {

	private TokenizedTree() {
	}

	public static Rcl getTree(Rcl rcl) {
		Rcl clone = rcl.clone();
		clone.recurse(new RclVisitor() {

			public void visit(Rcl parent, Entity entity) {
				entity.setId(null);
				entity.setReferenceId(null);
			}

			public void visit(Rcl parent, ActionAttribute actionAttribute) {
				actionAttribute.setAction(null);
			}

			public void visit(Rcl parent, ColorAttribute colorAttribute) {
				colorAttribute.setColor(null);
			}

			public void visit(Rcl parent, IndicatorAttribute indicatorAttribute) {
				indicatorAttribute.setIndicator(null);
			}

			public void visit(Rcl parent, RelationAttribute relationAttribute) {
				relationAttribute.setRelation(null);
			}

			public void visit(Rcl parent, TypeAttribute typeAttribute) {
				typeAttribute.setType(null);
			}

			public void visit(Rcl parent, OrdinalAttribute ordinalAttribute) {
				ordinalAttribute.setOrdinal(null);
			}

			public void visit(Rcl parent, CardinalAttribute cardinalAttribute) {
				cardinalAttribute.setCardinal(null);
			}
		});

		return clone;
	}
}