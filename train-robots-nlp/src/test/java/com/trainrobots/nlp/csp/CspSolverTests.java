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

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldModel;

public class CspSolverTests {

	@Test
	public void shouldSolveCube() {
		testSolution(828, "(entity: (type: cube))", 9);
	}

	@Test
	public void shouldSolvePrism() {
		testSolution(828, "(entity: (type: prism))", 3);
	}

	@Test
	@Ignore
	public void shouldSolveYellowCube() {
		testSolution(828, "(entity: (color: yellow) (type: cube))", 1);
	}

	private static void testSolution(int sceneNumber, String text,
			int expectedCount) {

		Csp csp = Csp.fromRcl(text);
		WorldModel world = SceneManager.getScene(sceneNumber).before;
		CspSolver solver = new CspSolver(world);
		Solution solution = solver.solve(csp);

		assertEquals(expectedCount, solution.size());
	}
}