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

package com.trainrobots.nlp.processor;

import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.scenes.Scene;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.moves.Move;

public class MoveValidator {

	private MoveValidator() {
	}

	public static void validate(int sceneNumber, Rcl rcl) {
		Scene scene = SceneManager.getScene(sceneNumber);
		List<Move> moves = Processor.getMoves(scene.before, rcl);
		if (!match(moves, scene.moves)) {
			throw new CoreException("Incorrect move.");
		}
	}

	private static boolean match(List<Move> moves1, List<Move> moves2) {
		int size1 = moves1 != null ? moves1.size() : 0;
		int size2 = moves2 != null ? moves2.size() : 0;
		if (size1 != size2) {
			return false;
		}
		for (int i = 0; i < size1; i++) {
			Move m1 = moves1.get(i);
			Move m2 = moves2.get(i);
			if (!m1.equals(m2)) {
				return false;
			}
		}
		return true;
	}
}