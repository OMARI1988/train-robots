/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.data.DataService;

public class NavigationTree extends JTree {

	public NavigationTree(DataService dataService, CommandService commandService) {

		// Model.
		setModel(new DefaultTreeModel(new TreebankNode(dataService,
				commandService)));

		// Show root handles.
		setShowsRootHandles(true);

		// Tree node renderer.
		TreeNodeRenderer treeNodeRenderer = new TreeNodeRenderer();
		setCellRenderer(treeNodeRenderer);

		// Selection listener.
		addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent event) {
				((TreeNode) event.getPath().getLastPathComponent()).select();
			}
		});

		// Key listener.
		addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent event) {
				handleKeyTyped(event.getKeyChar());
			}

			public void keyPressed(KeyEvent event) {
			}

			public void keyReleased(KeyEvent event) {
			}
		});
	}

	private void handleKeyTyped(char ch) {
		if (ch == '\n') {

			// Get tree node.
			TreePath path = getSelectionPath();
			TreeNode treeNode = (TreeNode) path.getLastPathComponent();
			if (!treeNode.isLeaf()) {

				// Collapse/expand non-leaf nodes.
				if (isExpanded(path)) {
					collapsePath(path);
				} else {
					expandPath(path);
				}
			}
		}
	}
}