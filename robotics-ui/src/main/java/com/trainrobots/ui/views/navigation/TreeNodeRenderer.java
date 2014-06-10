/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.trainrobots.ui.Resources;

public class TreeNodeRenderer extends DefaultTreeCellRenderer {

	private static final Icon FOLDER_ICON = new ImageIcon(
			Resources.url("/com/trainrobots/ui/icons/folder.gif"));

	private static final Icon LEAF_ICON = new ImageIcon(
			Resources.url("/com/trainrobots/ui/icons/leaf.gif"));

	public TreeNodeRenderer() {
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
		setForeground(selected ? node.color().brighter() : node.color());

		// Return renderer.
		return this;
	}
}