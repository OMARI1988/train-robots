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

package com.trainrobots.nlp.scenes;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.PickUpMove;

public class SceneTests {

	@Test
	public void shouldIdentifyDirectMove() {

		Scene scene = SceneManager.getScene(897);
		List<Move> moves = scene.moves;
		assertEquals(moves.size(), 1);

		DirectMove move = (DirectMove) moves.get(0);
		assertEquals(move.from, new Position(3, 6, 2));
		assertEquals(move.to, new Position(6, 7, 1));
	}

	@Test
	public void shouldIdentifyPickUpMove() {

		Scene scene = SceneManager.getScene(503);
		List<Move> moves = scene.moves;
		assertEquals(moves.size(), 1);

		PickUpMove move = (PickUpMove) moves.get(0);
		assertEquals(move.from, new Position(2, 1, 3));
	}
}