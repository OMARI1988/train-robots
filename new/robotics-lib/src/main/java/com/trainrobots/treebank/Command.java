/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import com.trainrobots.scenes.Scene;

public class Command {

	private final int id;
	private final Scene scene;
	private final String text;

	public Command(int id, Scene scene, String text) {
		this.id = id;
		this.scene = scene;
		this.text = text;
	}

	public int id() {
		return id;
	}

	public Scene scene() {
		return scene;
	}

	public String text() {
		return text;
	}
}