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
import com.trainrobots.core.rcl.Color;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.scenes.moves.DirectMove;
import com.trainrobots.nlp.scenes.moves.DropMove;
import com.trainrobots.nlp.scenes.moves.Move;
import com.trainrobots.nlp.scenes.moves.TakeMove;

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

	private static Scene buildScene(int number, WorldModel before,
			WorldModel after) {
		Scene scene = new Scene();
		scene.number = number;
		scene.before = before;
		scene.after = after;
		scene.moves = calculateMoves(scene);
		return scene;
	}

	private static List<Move> calculateMoves(Scene scene) {
		return calculateMoves(scene.before, scene.after);
	}

	public static List<Move> calculateMoves(WorldModel before, WorldModel after) {

		// Removed.
		List<Shape> removed = new ArrayList<Shape>();
		for (Shape shape : before.shapes()) {
			if (after.getShape(shape.position()) == null) {
				removed.add(shape);
			}
		}

		// Added.
		List<Shape> added = new ArrayList<Shape>();
		for (Shape shape : after.shapes()) {
			if (before.getShape(shape.position()) == null) {
				added.add(shape);
			}
		}

		// Invalid?
		if (removed.size() != added.size()) {
			throw new CoreException("Failed to identify move for scene.");
		}

		// Moves.
		List<Move> moves = null;
		for (Shape s1 : removed) {
			Shape s2 = find(s1, added);
			if (moves == null) {
				moves = new ArrayList<Move>();
			}

			if (s1.equals(before.getShapeInGripper())) {
				moves.add(new DropMove());
				continue;
			}

			if (s2.position().z > 0
					&& after.getShape(s2.position().add(0, 0, -1)) == null) {
				moves.add(new TakeMove(s1.position()));
				continue;
			}

			moves.add(new DirectMove(s1.position(), s2.position()));
		}
		return moves;
	}

	private static Shape find(Shape s, List<Shape> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Shape s2 = list.get(i);
			if (s.type() == s2.type() && s.color() == s2.color()) {
				return s2;
			}
		}
		throw new CoreException("Failed to find matching shape.");
	}

	private static WorldModel buildWorldModel(Configuration c) {

		List<Shape> shapes = new ArrayList<Shape>();
		for (Block b : c.blocks) {
			Shape s = new Shape(getColor(b.color),
					b.type == Block.CUBE ? Type.cube : Type.prism,
					new Position(b.x, b.y, b.z));
			shapes.add(s);
		}

		return new WorldModel(new Position(c.armX, c.armY, c.armZ),
				c.gripperOpen, shapes);
	}

	private static Color getColor(char color) {
		switch (color) {
		case Block.BLUE:
			return Color.blue;
		case Block.CYAN:
			return Color.cyan;
		case Block.RED:
			return Color.red;
		case Block.YELLOW:
			return Color.yellow;
		case Block.GREEN:
			return Color.green;
		case Block.MAGENTA:
			return Color.magenta;
		case Block.GRAY:
			return Color.gray;
		}
		return Color.white;
	}
}