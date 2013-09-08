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

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.configuration.Block;
import com.trainrobots.core.configuration.Configuration;
import com.trainrobots.core.configuration.ConfigurationReader;

public class SceneManager {

	private static final Scene[] scenes = new Scene[1000];

	private SceneManager() {
	}

	public static Scene getScene(int sceneNumber) {
		return scenes[sceneNumber - 1];
	}

	static {

		// Load items.
		WorldModel[][] items = new WorldModel[125][5];
		for (Configuration c : ConfigurationReader
				.read("../data/configuration.txt")) {
			items[c.groupNumber - 1][c.imageNumber - 1] = buildWorldModel(c);
		}

		// Scenes.
		int i = 0;
		for (int g = 0; g < 125; g++) {
			for (int c = 1; c < 5; c++) {
				scenes[i] = buildScene(++i, items[g][0], items[g][c]);
				scenes[i] = buildScene(++i, items[g][c], items[g][0]);
			}
		}
	}

	private static Scene buildScene(int sceneNumber, WorldModel before,
			WorldModel after) {
		Scene scene = new Scene();
		scene.sceneNumber = sceneNumber;
		scene.before = before;
		scene.after = after;
		scene.moves = calculateMoves(scene);
		return scene;
	}

	private static List<Move> calculateMoves(Scene scene) {

		// Configuration.
		WorldModel before = scene.before;
		WorldModel after = scene.after;

		// Removed.
		List<Shape> removed = new ArrayList<Shape>();
		for (Shape shape : before.shapes()) {
			if (after.getShape(shape.position) == null) {
				removed.add(shape);
			}
		}

		// Added.
		List<Shape> added = new ArrayList<Shape>();
		for (Shape shape : after.shapes()) {
			if (before.getShape(shape.position) == null) {
				added.add(shape);
			}
		}

		// Invalid?
		if (removed.size() != added.size()) {
			throw new CoreException("Failed to identify move for scene "
					+ scene.sceneNumber + ".");
		}

		// Moves.
		List<Move> moves = null;
		for (Shape s1 : removed) {
			Shape s2 = find(s1, added);
			if (moves == null) {
				moves = new ArrayList<Move>();
			}
			moves.add(new Move(s1.position, s2.position));
		}
		return moves;
	}

	private static Shape find(Shape s, List<Shape> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Shape s2 = list.get(i);
			if (s.cube == s2.cube && s.color == s2.color) {
				return s2;
			}
		}
		throw new CoreException("Failed to find matching shape.");
	}

	private static WorldModel buildWorldModel(Configuration c) {

		List<Shape> shapes = new ArrayList<Shape>();
		for (Block b : c.blocks) {
			Shape s = new Shape(getColor(b.color), b.type == Block.CUBE,
					new Position(b.x, b.y, b.z));
			shapes.add(s);
		}

		return new WorldModel(new Position(c.armX, c.armY, c.armZ),
				c.gripperOpen, shapes);
	}

	private static Color getColor(char color) {
		switch (color) {
		case Block.BLUE:
			return Color.Blue;
		case Block.CYAN:
			return Color.Cyan;
		case Block.RED:
			return Color.Red;
		case Block.YELLOW:
			return Color.Yellow;
		case Block.GREEN:
			return Color.Green;
		case Block.MAGENTA:
			return Color.Magenta;
		case Block.GRAY:
			return Color.Gray;
		}
		return Color.White;
	}
}