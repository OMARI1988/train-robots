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

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.MarkType;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.AnnotationWindow;
import com.trainrobots.ui.views.tree.TreeNode;

public class CommandNode extends TreeNode {

	private final WindowService windowService;
	private final Command command;

	public CommandNode(WindowService windowService, Command command) {
		super("C" + command.id, true);
		this.windowService = windowService;
		this.command = command;
		decorate();
	}

	public Command getComand() {
		return command;
	}

	@Override
	public void select() {
		windowService.get(AnnotationWindow.class).getCorpusView().select(command);
	}

	public void decorate() {
		if (command.mark == MarkType.Accurate) {
			setColor(DARK_GREEN);
		} else if (command.mark == MarkType.Unmarked) {
			setColor(command.enhancement == 0 ? Color.BLACK : DARK_ORANGE);
		} else {
			setColor(Color.GRAY);
		}
	}
}
