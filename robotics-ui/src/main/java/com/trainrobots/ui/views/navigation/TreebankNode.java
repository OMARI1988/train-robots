/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import com.trainrobots.RoboticException;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Treebank;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.data.DataService;

public class TreebankNode extends TreeNode {

	private final DataService dataService;
	private final CommandService commandService;

	public TreebankNode(DataService dataService, CommandService commandService) {
		super("Treebank", false);
		this.dataService = dataService;
		this.commandService = commandService;
	}

	public SceneNode child(Scene scene) {
		int size = getChildCount();
		for (int i = 0; i < size; i++) {
			SceneNode sceneNode = (SceneNode) getChildAt(i);
			if (sceneNode.scene() == scene) {
				return sceneNode;
			}
		}
		throw new RoboticException(
				"Failed to find child node for scene ID '%d'.", scene.id());
	}

	@Override
	protected void createChildNodes() {
		Treebank treebank = dataService.treebank();
		for (Scene scene : treebank.scenes()) {
			add(new SceneNode(scene, commandService, treebank.commands(scene)));
		}
	}
}