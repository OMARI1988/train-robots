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

package com.trainrobots.ui.views.tree.corpus;

import javax.inject.Inject;
import javax.swing.tree.TreePath;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.ui.services.ConfigurationService;
import com.trainrobots.ui.services.CorpusService;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.tree.TreeView;

public class CorpusTreeView extends TreeView {

	@Inject
	public CorpusTreeView(ConfigurationService configurationServie,
			CorpusService corpusService, WindowService windowService) {
		super(new RootNode(configurationServie, corpusService, windowService));
	}

	public void selectCommand(Command command) {

		// Root node.
		RootNode rootNode = (RootNode) getModel().getRoot();
		rootNode.getChildCount();

		// Scene node.
		SceneNode sceneNode = (SceneNode) rootNode
				.getChildAt(command.sceneNumber - 1);

		// Command node.
		CommandNode commandNode = null;
		int size = sceneNode.getChildCount();
		for (int i = 0; i < size; i++) {
			CommandNode node = (CommandNode) sceneNode.getChildAt(i);
			if (node.getComand() == command) {
				commandNode = node;
			}
		}

		// Select.
		if (commandNode != null) {
			TreePath selectionPath = new TreePath(commandNode.getPath());
			setSelectionPath(selectionPath);
			scrollPathToVisible(selectionPath);
		}
	}
}