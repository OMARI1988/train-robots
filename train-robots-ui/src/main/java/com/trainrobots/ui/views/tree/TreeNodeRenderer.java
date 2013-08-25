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

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.trainrobots.ui.resources.ResourceUtil;

class TreeNodeRenderer extends DefaultTreeCellRenderer {

	private static final Icon FOLDER_ICON = new ImageIcon(
			ResourceUtil.getUrl("/com/trainrobots/ui/folder.gif"));

	private static final Icon LEAF_ICON = new ImageIcon(
			ResourceUtil.getUrl("/com/trainrobots/ui/leaf.gif"));

	public TreeNodeRenderer() {

		// Defaults.
		setLeafIcon(leafIcon);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		// Render parent.
		super.getTreeCellRendererComponent(tree, value, selected, expanded,
				leaf, row, hasFocus);

		// Icon.
		TreeNode node = (TreeNode) value;
		setIcon(node.isLeaf() ? LEAF_ICON : FOLDER_ICON);

		// Return renderer.
		return this;
	}
}
