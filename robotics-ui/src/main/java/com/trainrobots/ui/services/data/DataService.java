/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.data;

import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Treebank;

public class DataService {

	private final Treebank treebank = new Treebank("../.data");
	private Scene selectedScene;

	public DataService() {
		selectedScene = treebank.scene(879);
	}

	public Treebank treebank() {
		return treebank;
	}

	public Scene selectedScene() {
		return selectedScene;
	}
}