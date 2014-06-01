/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.trainrobots.collections.ArrayIterator;
import com.trainrobots.collections.Items;

public class Scenes implements Items<Scene> {

	private final Scene[] scenes;
	private final Map<Integer, Scene> map = new HashMap<>();

	public Scenes(Items<Scene> scenes) {
		this.scenes = scenes.toArray();
		for (Scene scene : scenes) {
			map.put(scene.id(), scene);
		}
	}

	@Override
	public int count() {
		return scenes.length;
	}

	@Override
	public Scene get(int index) {
		return scenes[index];
	}

	public Scene scene(int id) {
		Scene scene = map.get(id);
		if (scene == null) {
			throw new IllegalArgumentException(String.format(
					"The scene ID '%d' is not recognized.", id));
		}
		return scene;
	}

	@Override
	public Scene[] toArray() {
		return scenes.clone();
	}

	@Override
	public Iterator<Scene> iterator() {
		return new ArrayIterator(scenes);
	}
}