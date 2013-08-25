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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import com.trainrobots.ui.resources.ResourceUtil;

public class TreeMenu extends JPopupMenu implements ActionListener {

	private final TreeView treeView;
	private final TreePath treePath;
	private final TreeNode treeNode;

	@Inject
	public TreeMenu(TreeView treeView) {

		// Selected node.
		this.treeView = treeView;
		this.treePath = treeView.getSelectionPath();
		this.treeNode = (TreeNode) treePath.getLastPathComponent();

		// Expand.
		String name = treeView.isExpanded(treePath) ? "Collapse" : "Expand";
		addItem("/com/trainrobots/ui/expand.gif", name, "ENTER",
				!treeNode.isLeaf());
	}

	public void actionPerformed(ActionEvent event) {

		// Execute.
		String name = event.getActionCommand();
		if (name.equals("Expand")) {
			treeView.expandPath(treePath);
		} else if (name.equals("Collapse")) {
			treeView.collapsePath(treePath);
		}
	}

	private void addItem(String icon, String name, String shortCutKey,
			boolean isEnabled) {

		// Create item.
		JMenuItem item = new JMenuItem(name);
		item.setIcon(new ImageIcon(ResourceUtil.getUrl(icon)));
		item.setAccelerator(KeyStroke.getKeyStroke(shortCutKey));
		item.setEnabled(isEnabled);
		item.addActionListener(this);

		// Add item.
		add(item);
	}
}
