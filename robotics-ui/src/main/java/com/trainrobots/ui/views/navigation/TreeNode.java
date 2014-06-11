/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import java.awt.Color;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class TreeNode extends DefaultMutableTreeNode {

	private final boolean isLeaf;
	private boolean initiated;
	protected String name;

	protected TreeNode(boolean isLeaf) {
		this(null, isLeaf);
	}

	protected TreeNode(String name, boolean isLeaf) {
		this.name = name;
		this.isLeaf = isLeaf;
	}

	public String name() {
		return name;
	}

	public Color color() {
		return Color.BLACK;
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