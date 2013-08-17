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

package com.trainrobots.web.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.trainrobots.web.game.Scene;

public class GameService {

	private final List<Scene> scenes = new ArrayList<Scene>();

	public GameService() {

		// Add.
		/* 1 */ add(5, "g1/x1", "g1/x2", "Put the small red circle above the blue circle.");
		/* 2 */ add(5, "g1/x2", "g1/x1", "Move the small red circle to the top left of the orange square.");
		/* 3 */ add(0, "g1/x1", "g1/x3", "I don't like this game");
		/* 4 */ add(1, "g1/x3", "g1/x1", "This game is stupid");
		/* 5 */ add(1, "g1/x1", "g1/x4", "Make me a cup of tea");
		/* 6 */ add(4, "g1/x4", "g1/x1", "Move the red circle above blue one");
		/* 7 */ add(0, "g1/x1", "g1/x5", "Move green circle to right edge");
		/* 8 */ add(2, "g1/x5", "g1/x1", "Put orange square left");
		/* 9 */ add(0, "g2/x1", "g2/x2", "Let the blue circle go bottom left of board but at one square to right.");
		/* 10 */ add(4, "g2/x2", "g2/x1", "Move red circle right.");
		/* 11 */ add(2, "g2/x1", "g2/x3", "Put green circle on red one.");
		/* 12 */ add(0, "g2/x3", "g2/x1", "Move the green circle to bottom edge");
		/* 13 */ add(4, "g2/x1", "g2/x4", "Move to the green circle to the top right of the board.");
		/* 14 */ add(4, "g2/x4", "g2/x1", "Move the blue circle to the bottom of the board at the very left edge");
		/* 15 */ add(0, "g2/x1", "g2/x5", "Position the orange shape two below greed circle");
		/* 16 */ add(3, "g2/x5", "g2/x1", "Move orange top left");
	}

	public int randomSceneNumber() {
		return 1 + new Random().nextInt(scenes.size());
	}

	public Scene scene(int sceneNumber) {
		return scenes.get(sceneNumber - 1);
	}

	private void add(int mark, String image1, String image2, String description) {
		Scene scene = new Scene();
		scene.mark = mark;
		scene.image1 = "/static/" + image1 + ".png";
		scene.image2 = "/static/" + image2 + ".png";
		scene.description = description;
		scenes.add(scene);
	}
}