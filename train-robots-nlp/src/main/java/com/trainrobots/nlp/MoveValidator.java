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

package com.trainrobots.nlp;

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.csp.Csp;
import com.trainrobots.nlp.csp.Model;
import com.trainrobots.nlp.scenes.Position;
import com.trainrobots.nlp.scenes.Scene;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.DropMove;
import com.trainrobots.nlp.scenes.moves.Move;

public class MoveValidator {

	private MoveValidator() {
	}

	public static void validate(int sceneNumber, Rcl rcl) {
		Scene scene = SceneManager.getScene(sceneNumber);
		Model model = new Model(scene.before);
		List<Move> moves = Csp.fromAction(rcl, rcl).solve(model);
		if (!match(scene.before, scene.moves, moves)) {
			throw new CoreException("Incorrect move.");
		}
	}

	public static boolean match(WorldModel world, List<Move> expectedMoves,
			List<Move> actualMoves) {

		// Sizes.
		int size1 = expectedMoves != null ? expectedMoves.size() : 0;
		int size2 = actualMoves != null ? actualMoves.size() : 0;

		// No move specified?
		if (size2 == 1) {
			if (actualMoves.get(0) instanceof DirectMove) {
				DirectMove actualMove = (DirectMove) actualMoves.get(0);
				if (actualMove.from.equals(actualMove.to)) {
					throw new CoreException("Zero move specified.");
				}
			}
		}

		// Direct move vs drop.
		if (size1 == 1 && size2 == 1
				&& expectedMoves.get(0) instanceof DropMove
				&& actualMoves.get(0) instanceof DirectMove) {
			DirectMove actualMove = (DirectMove) actualMoves.get(0);
			Position expectedFrom = world.getShapeInGripper().position();
			if (actualMove.from.equals(expectedFrom)
					&& actualMove.to.equals(world.getDropPosition(
							expectedFrom.x, expectedFrom.y))) {
				return true;
			}
		}

		// Compare.
		if (size1 != size2) {
			return false;
		}
		for (int i = 0; i < size1; i++) {
			Move expectedMove = expectedMoves.get(i);
			Move actualMove = actualMoves.get(i);
			if (!actualMove.equals(expectedMove)) {
				return false;
			}
		}
		return true;
	}
}