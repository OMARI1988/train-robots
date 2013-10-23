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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.trainrobots.core.rcl.Entity;

public class CspConverterTests {

	@Test
	public void shouldConvertEntity() {

		Entity entity = Entity.fromString("(entity: (type: cube))");
		EntityNode entityNode = CspConverter.convert(entity, entity);
		assertEquals(entityNode.toString(), "(entity: (type: cube))");
	}

	@Test
	public void shouldConvertEntityWithRelation() {

		Entity entity = Entity
				.fromString("(entity: (color: blue) (type: cube) (spatial-relation: (relation: above) (entity: (type: board))))");
		EntityNode entityNode = CspConverter.convert(entity, entity);

		assertEquals(
				entityNode.toString(),
				"(entity: (type: cube) (color: blue) (relation: above (entity: (type: board))))");
	}
}