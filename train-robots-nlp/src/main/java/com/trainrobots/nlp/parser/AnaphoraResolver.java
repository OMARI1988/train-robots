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

import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.Type;

public class AnaphoraResolver {

	private AnaphoraResolver() {
	}

	public static void resolve(final Rcl rcl) {

		rcl.recurse(new RclVisitor() {

			private Entity last;

			@Override
			public void visit(Rcl parent, Entity entity) {

				if (entity.isType(Type.reference)
						|| entity.isType(Type.typeReference)
						|| entity.isType(Type.typeReferenceGroup)) {

					if (rcl instanceof Sequence) {
						Sequence s = (Sequence) rcl;
						Entity arg1 = s.events().get(0).entity();
						map(entity, arg1);
					} else if (last != null) {
						map(entity, last);
					}
				}
				last = entity;
			}
		});
	}

	private static void map(Entity entity, Entity reference) {
		reference.setId(1);
		entity.setReferenceId(1);
	}
}