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

package com.trainrobots.nlp.planning;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.trainrobots.core.rcl.Entity;
import com.trainrobots.nlp.planning.Planner;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldEntity;
import com.trainrobots.nlp.scenes.WorldModel;

public class PlannerTests {

	@Test
	public void shouldGroundCube() {
		testGrounding(828, "(entity: (type: cube))", 9);
	}

	@Test
	public void shouldGroundPrism() {
		testGrounding(828, "(entity: (type: prism))", 3);
	}

	@Test
	public void shouldGroundYellowCube() {
		testGrounding(828, "(entity: (color: yellow) (type: cube))", 1);
	}

	@Test
	public void shouldGroundRedCube() {
		testGrounding(828, "(entity: (color: red) (type: cube))", 5);
	}

	@Test
	public void shouldGroundGrayPrism() {
		testGrounding(828, "(entity: (color: gray) (type: prism))", 3);
	}

	@Test
	public void shouldGroundYellowPrism() {
		testGrounding(828, "(entity: (color: yellow) (type: prism))", 0);
	}

	@Test
	public void shouldGroundCorner() {
		WorldEntity grounding = getSingleGrounding(828,
				"(entity: (indicator: back) (indicator: left) (type: corner))");
		assertEquals(grounding, Corner.BackLeft);
	}

	@Test
	public void shouldGroundBlueCubeAboveBoard() {
		testGrounding(
				337,
				"(entity: (color: blue) (type: cube) (spatial-relation: (relation: above) (entity: (type: board))))",
				1);
	}

	@Test
	public void shouldGroundYellowPrismAboveRedCube() {
		testGrounding(
				177,
				"(entity: (color: yellow) (type: prism) (spatial-relation: (relation: above) (entity: (color: red) (type: cube))))",
				1);
	}

	@Test
	public void shouldGroundGrayCubeAboveBackLeftCorner() {
		testGrounding(
				849,
				"(entity: (color: gray) (type: cube) (spatial-relation: (relation: above) (entity: (indicator: back) (indicator: left) (type: corner))))",
				1);
	}

	@Test
	public void shouldGroundCompositeColoredStack() {
		testGrounding(
				698,
				"(entity: (color: yellow) (color: blue) (color: gray) (type: stack))",
				1);
	}

	@Test
	public void shouldGroundRobot() {
		testGrounding(1, "(entity: (type: robot))", 1);
	}

	@Test
	public void shouldGroundGreenCubeNearestRobot() {
		testGrounding(
				563,
				"(entity: (color: green) (type: cube) (spatial-relation: (relation: nearest) (entity: (type: robot)))))",
				1);
	}

	@Test
	public void shouldGroundIndividualGreenBlock() {
		testGrounding(
				214,
				"(entity: (indicator: individual) (color: green) (type: cube))",
				1);
	}

	private static WorldEntity getSingleGrounding(int sceneNumber, String text) {
		WorldModel world = SceneManager.getScene(sceneNumber).before;
		Entity entity = Entity.fromString(text);
		List<WorldEntity> list = new Planner(world).ground(entity, entity);
		assertEquals(list.size(), 1);
		return list.get(0);
	}

	private static void testGrounding(int sceneNumber, String text,
			int expectedCount) {

		WorldModel world = SceneManager.getScene(sceneNumber).before;
		Entity entity = Entity.fromString(text);
		List<WorldEntity> list = new Planner(world).ground(entity, entity);
		assertEquals(expectedCount, list.size());
	}
}