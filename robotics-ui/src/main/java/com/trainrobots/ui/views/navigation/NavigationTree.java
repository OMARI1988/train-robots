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

import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandAware;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.treebank.TreebankService;

public class NavigationTree extends JTree implements CommandAware {

	private final CommandService commandService;

	public NavigationTree(TreebankService treebankService,
			CommandService commandService) {

		// Services.
		this.commandService = commandService;

		// Model.
		setModel(new DefaultTreeModel(new TreebankNode(treebankService,
				commandService)));

		// Show root handles.
		setShowsRootHandles(true);

		// Tree node renderer.
		setCellRenderer(new TreeNodeRenderer());

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

	public void refresh() {

		// Command.
		Command command = commandService.command();

		// Scene.
		TreebankNode treebankNode = (TreebankNode) getModel().getRoot();
		SceneNode sceneNode = treebankNode.child(command.scene());

		// Refresh.
		sceneNode.refresh();
		repaint();
	}

	@Override
	public void bindTo(Command command) {

		// Path.
		TreebankNode treebankNode = (TreebankNode) getModel().getRoot();
		SceneNode sceneNode = treebankNode.child(command.scene());
		CommandNode commandNode = sceneNode.child(command);
		Object[] nodes = { treebankNode, sceneNode, commandNode };
		TreePath commandPath = new TreePath(nodes);

		// Already selected?
		if (commandPath.equals(getSelectionPath())) {
			return;
		}

		// Expand.
		TreePath scenePath = commandPath.getParentPath();
		expandPath(scenePath);

		// Select.
		setSelectionPath(commandPath);

		// Try to make both the scene and command nodes visible.
		scrollPathToVisible(scenePath);
		scrollPathToVisible(commandPath);
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