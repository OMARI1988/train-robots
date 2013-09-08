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

import com.trainrobots.core.configuration.Configuration;
import com.trainrobots.core.configuration.ConfigurationReader;

public class SceneManager {

	private static final Scene[] scenes = new Scene[1000];

	private SceneManager() {
	}

	public static Scene get(int sceneNumber) {
		return scenes[sceneNumber - 1];
	}

	static {

		// Load items.
		Configuration[][] items = new Configuration[125][5];
		for (Configuration c : ConfigurationReader
				.read("../data/configuration.txt")) {
			items[c.groupNumber - 1][c.imageNumber - 1] = c;
		}

		// Scenes.
		int i = 0;
		for (int g = 0; g < 125; g++) {
			for (int c = 1; c < 5; c++) {

				// Forward.
				Scene sceneA = new Scene();
				sceneA.before = items[g][0];
				sceneA.after = items[g][c];
				scenes[i++] = sceneA;

				// Backward.
				Scene sceneB = new Scene();
				sceneB.before = items[g][c];
				sceneB.after = items[g][0];
				scenes[i++] = sceneB;
			}
		}
	}
}