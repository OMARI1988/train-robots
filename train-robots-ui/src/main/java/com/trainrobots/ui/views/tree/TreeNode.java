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

package com.trainrobots.ui.views.tree;

import java.awt.Color;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class TreeNode extends DefaultMutableTreeNode {

	private final boolean isLeaf;
	private boolean isInitiated = false;
	private String name;
	private Color color = Color.BLACK;

	protected TreeNode(String name, boolean isLeaf) {
		this.name = name;
		this.isLeaf = isLeaf;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isLeaf() {
		return isLeaf;
	}

	@Override
	public int getChildCount() {

		// Initiate child nodes.
		if (!isInitiated) {

			// Mark as initiated before creating child nodes.
			isInitiated = true;

			// Create child nodes.
			createChildNodes();
		}

		// Return child count.
		return super.getChildCount();
	}

	public void select() {
	}

	@Override
	public String toString() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	protected void createChildNodes() {
	}
}
