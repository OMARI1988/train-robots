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

package com.trainrobots.nlp.grounding;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.rcl.Entity;
import com.trainrobots.nlp.scenes.Corner;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldModel;

public class GrounderTests {

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
		Grounding grounding = getSingleGrounding(828,
				"(entity: (spatial-indicator: back) (spatial-indicator: left) (type: corner))");
		assertEquals(grounding.entity(), Corner.BackLeft);
	}

	@Test
	public void shouldGroundBlueStack() {
		testGrounding(3, "(entity: (color: blue) (type: stack))", 1);
	}

	@Test
	public void shouldGroundBlueCubeAboveBoard() {
		testGrounding(
				337,
				"(entity: (color: blue) (type: cube) (spatial-relation: (spatial-indicator: above) (entity: (type: board)))) (event: (action: take) (entity: (color: blue) (type: cube) (spatial-relation: (spatial-indicator: above) (entity: (type: board)))))",
				1);
	}

	@Test
	public void shouldGroundYellowPrismAboveRedCube() {
		testGrounding(
				177,
				"(entity: (color: yellow) (type: prism) (spatial-relation: (spatial-indicator: above) (entity: (color: red) (type: cube))))",
				1);
	}

	@Test
	public void shouldGroundGrayCubeAboveBackLeftCorner() {
		testGrounding(
				849,
				"(entity: (color: gray) (type: cube) (spatial-relation: (spatial-indicator: above) (entity: (spatial-indicator: back) (spatial-indicator: left) (type: corner))))",
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
	@Ignore
	public void shouldGroundTopPartOfStack() {
		testGrounding(
				3,
				"(entity: (spatial-indicator: top) (type: cube) (spatial-relation: (spatial-indicator: part) (entity: (color: blue) (type: stack))))",
				1);
	}

	private static Grounding getSingleGrounding(int sceneNumber, String text) {
		WorldModel world = SceneManager.getScene(sceneNumber).before;
		Entity entity = Entity.fromString(text);
		List<Grounding> list = new Grounder(world).ground(entity, entity);
		assertEquals(list.size(), 1);
		return list.get(0);
	}

	private static void testGrounding(int sceneNumber, String text,
			int expectedCount) {

		WorldModel world = SceneManager.getScene(sceneNumber).before;
		Entity entity = Entity.fromString(text);
		List<Grounding> list = new Grounder(world).ground(entity, entity);
		assertEquals(expectedCount, list.size());
	}
}