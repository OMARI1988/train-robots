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

package com.trainrobots.nlp.parsing;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.corpus.MarkType;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.MoveValidator;
import com.trainrobots.nlp.csp.Csp;
import com.trainrobots.nlp.csp.Model;
import com.trainrobots.nlp.parser.GoldParser;
import com.trainrobots.nlp.scenes.Scene;
import com.trainrobots.nlp.scenes.SceneManager;
import com.trainrobots.nlp.scenes.WorldModel;
import com.trainrobots.nlp.scenes.moves.Move;

public class ImageConfusionTests {

	@Test
	@Ignore
	public void shouldFindImageConfusion() {

		// Parse.
		for (Command command : Corpus.getCommands()) {

			// Already marked?
			if (command.rcl != null || command.mark != MarkType.Unmarked) {
				continue;
			}

			// Parse.
			WorldModel world = SceneManager.getScene(command.sceneNumber).after;
			Rcl rcl;
			try {
				rcl = GoldParser.parse(world, command.text).rcl();
			} catch (Exception e) {
				continue;
			}
			try {
				Scene scene = SceneManager.getScene(command.sceneNumber);
				Model model = new Model(scene.after);
				List<Move> moves = Csp.fromAction(rcl, rcl).solve(model);
				if (!MoveValidator.match(scene.after,
						SceneManager.calculateMoves(scene.after, scene.before),
						moves)) {
					throw new CoreException("Incorrect move.");
				}
				System.out.println(command.id);
			} catch (Exception e) {
			}
		}
	}
}