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

import java.awt.Color;
import java.util.List;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.MarkType;
import com.trainrobots.ui.services.CorpusService;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.tree.TreeNode;

public class SceneNode extends TreeNode {

	private final CorpusService corpusService;
	private final WindowService windowService;
	private final int sceneNumber;

	public SceneNode(CorpusService corpusService, WindowService windowService,
			int sceneNumber) {
		super(generateName(corpusService, sceneNumber), false);
		this.corpusService = corpusService;
		this.windowService = windowService;
		this.sceneNumber = sceneNumber;
		decorate();
	}

	@Override
	protected void createChildNodes() {
		List<Command> commands = corpusService.getCommands(sceneNumber);
		if (commands == null) {
			return;
		}
		for (Command command : commands) {
			add(new CommandNode(windowService, command));
		}
	}

	public void decorate() {
		setName(generateName(corpusService, sceneNumber));
		setColor(calculateColor());
	}

	private Color calculateColor() {

		List<Command> commands = corpusService.getCommands(sceneNumber);
		if (commands == null) {
			return Color.BLACK;
		}

		boolean enhancement = false;
		for (Command command : commands) {
			if (command.enhancement != 0) {
				enhancement = true;
			} else if (command.mark == MarkType.Unmarked) {
				return Color.BLACK;
			}
		}

		return enhancement ? DARK_ORANGE : DARK_GREEN;
	}

	private static String generateName(CorpusService corpusService,
			int sceneNumber) {

		StringBuilder text = new StringBuilder();
		text.append("S");
		text.append(sceneNumber);
		text.append(" (");

		int accurate = 0;
		int unmarked = 0;
		for (Command command : corpusService.getCommands(sceneNumber)) {
			if (command.mark == MarkType.Accurate) {
				accurate++;
			} else if (command.mark == MarkType.Unmarked) {
				unmarked++;
			}
		}

		if (unmarked == 0) {
			text.append("completed");
		} else {
			text.append(accurate);
			text.append(" / ");
			text.append(accurate + unmarked);
		}

		text.append(')');
		return text.toString();
	}
}