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

import java.text.DecimalFormat;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.nlp.planning.Model;
import com.trainrobots.nlp.planning.Planner;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldEntity;
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
	@Ignore
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

	private int count = 0;
	private int valid = 0;

	@Test
	public void shouldSolveCorpus() {
		for (final Command command : Corpus.getCommands()) {
			if (command.rcl == null) {
				continue;
			}
			command.rcl.recurse(new RclVisitor() {

				public void visit(Rcl parent, Entity entity) {

					// World.
					WorldModel world = SceneManager
							.getScene(command.sceneNumber).before;

					// Expected.
					Planner planner = new Planner(world);
					List<WorldEntity> expected;
					try {
						expected = planner.ground(command.rcl, entity);
					} catch (Exception exception) {
						return;
					}
					count++;

					// Actual.
					Csp csp = Csp.fromRcl(command.rcl, entity);
					List<WorldEntity> actual = csp.solve(new Model(world));
					if (actual.size() == expected.size()) {
						valid++;
					}
				}
			});
		}

		// Score.
		DecimalFormat df = new DecimalFormat("#.##");
		double p = 100 * valid / (double) count;
		System.out.println("CSP score: " + valid + " / " + count + " = "
				+ df.format(p) + " %");
	}

	private static void testSolution(int sceneNumber, String text,
			int expectedCount) {

		Csp csp = Csp.fromRcl(text);
		Model model = new Model(SceneManager.getScene(sceneNumber).before);
		List<WorldEntity> entities = csp.solve(model);

		assertEquals(expectedCount, entities.size());
	}
}