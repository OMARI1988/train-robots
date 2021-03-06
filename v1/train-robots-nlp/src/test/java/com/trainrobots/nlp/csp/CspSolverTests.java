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

import java.util.List;

import org.junit.Test;

import com.trainrobots.core.rcl.Entity;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldEntity;

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
	public void shouldSolveYellowCube() {
		testSolution(828, "(entity: (color: yellow) (type: cube))", 1);
	}

	@Test
	public void shouldSolveRedCube() {
		testSolution(828, "(entity: (color: red) (type: cube))", 5);
	}

	@Test
	public void shouldSolveGrayPrism() {
		testSolution(828, "(entity: (color: gray) (type: prism))", 3);
	}

	@Test
	public void shouldSolveYellowPrism() {
		testSolution(828, "(entity: (color: yellow) (type: prism))", 0);
	}

	@Test
	public void shouldSolveBackLeftCorner() {
		testSolution(828,
				"(entity: (indicator: back) (indicator: left) (type: corner))",
				1);
	}

	@Test
	public void shouldSolveBlueCubeAboveBoard() {
		testSolution(
				337,
				"(entity: (color: blue) (type: cube) (spatial-relation: (relation: above) (entity: (type: board))))",
				1);
	}

	@Test
	public void shouldSolveYellowPrismAboveRedCube() {
		testSolution(
				177,
				"(entity: (color: yellow) (type: prism) (spatial-relation: (relation: above) (entity: (color: red) (type: cube))))",
				1);
	}

	@Test
	public void shouldSolveGrayCubeAboveBackLeftCorner() {
		testSolution(
				849,
				"(entity: (color: gray) (type: cube) (spatial-relation: (relation: above) (entity: (indicator: back) (indicator: left) (type: corner))))",
				1);
	}

	@Test
	public void shouldSolveCompositeColoredStack() {
		testSolution(
				698,
				"(entity: (color: yellow) (color: blue) (color: gray) (type: stack))",
				1);
	}

	@Test
	public void shouldSolveRobot() {
		testSolution(1, "(entity: (type: robot))", 1);
	}

	@Test
	public void shouldSolveGreenCubeNearestRobot() {
		testSolution(
				563,
				"(entity: (color: green) (type: cube) (spatial-relation: (relation: nearest) (entity: (type: robot)))))",
				1);
	}

	@Test
	public void shouldSolveIndividualGreenBlock() {
		testSolution(
				214,
				"(entity: (indicator: individual) (color: green) (type: cube))",
				1);
	}

	private static void testSolution(int sceneNumber, String text,
			int expectedCount) {

		Entity entity = Entity.fromString(text);
		EntityNode entityNode = Csp.fromEntity(entity, entity);
		Model model = new Model(SceneManager.getScene(sceneNumber).before);
		List<WorldEntity> entities = entityNode.solve(model);

		assertEquals(expectedCount, entities.size());
	}
}