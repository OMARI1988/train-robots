/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class TreeNode extends DefaultMutableTreeNode {

	private final String name;
	private final boolean isLeaf;
	private boolean initiated;

	protected TreeNode(String name, boolean isLeaf) {
		this.name = name;
		this.isLeaf = isLeaf;
	}

	public String name() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean isLeaf() {
		return isLeaf;
	}

	public void select() {
	}

	@Override
	public int getChildCount() {
		if (!initiated) {
			initiated = true;
			createChildNodes();
		}
		return super.getChildCount();
	}

	protected void createChildNodes() {
	}
}