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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public abstract class TreeView extends JTree implements TreeSelectionListener,
		KeyListener, MouseListener {

	protected TreeView(TreeNode rootNode) {

		// Model.
		setModel(new DefaultTreeModel(rootNode));

		// Show root handles.
		setShowsRootHandles(true);

		// Tree node renderer.
		TreeNodeRenderer treeNodeRenderer = new TreeNodeRenderer();
		setCellRenderer(treeNodeRenderer);

		// Events handlers.
		addTreeSelectionListener(this);
		addKeyListener(this);
		addMouseListener(this);
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {

		// Get node.
		TreePath path = event.getPath();
		TreeNode node = (TreeNode) path.getLastPathComponent();

		// Select.
		node.select();
	}

	public void keyPressed(KeyEvent event) {
	}

	public void keyReleased(KeyEvent event) {
	}

	public void keyTyped(KeyEvent event) {
		if (event.getKeyChar() == '\n') {

			// Get tree node.
			TreePath path = getSelectionPath();
			TreeNode treeNode = (TreeNode) path.getLastPathComponent();
			if (!treeNode.isLeaf()) {

				// Expand non-leaf nodes.
				if (isExpanded(path)) {
					collapsePath(path);
				} else {
					expandPath(path);
				}
			}
		}
	}

	public void mouseClicked(MouseEvent event) {
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mousePressed(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON3) {
			TreePath path = getPathForLocation(event.getX(), event.getY());
			setSelectionPath(path);
		}
	}

	public void mouseReleased(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON3) {
			TreePath path = getSelectionPath();
			if (path != null) {
				new TreeMenu(this).show(this, event.getX(), event.getY());
			}
		}
	}
}