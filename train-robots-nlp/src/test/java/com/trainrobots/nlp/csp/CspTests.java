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

import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.csp.Csp;
import com.trainrobots.nlp.csp.CspVariable;
import com.trainrobots.nlp.csp.constraints.TypeConstraint;

public class CspTests {

	@Test
	public void shouldFormatCsp() {

		Csp csp = new Csp();
		CspVariable x1 = csp.add();
		x1.add(new TypeConstraint(Type.cube));

		assertEquals(csp.toString(), "(csp (var x1 (type cube)))");
	}

	@Test
	public void shouldConvertRcl() {

		Csp csp = Csp.fromRcl("(entity: (type: cube))");
		assertEquals(csp.toString(), "(csp (var x1 (type cube)))");
	}
}