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

package com.trainrobots.ui.commands;

import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.ui.services.CorpusService;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.AnnotationWindow;
import com.trainrobots.ui.views.StatusBar;

public class SearchCommand extends AbstractAction {

	private final WindowService windowService;
	private final CorpusService corpusService;

	@Inject
	public SearchCommand(WindowService windowService,
			CorpusService corpusService) {
		this.windowService = windowService;
		this.corpusService = corpusService;
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		// Try.
		AnnotationWindow mainWindow = windowService.get(AnnotationWindow.class);
		StatusBar statusBar = mainWindow.getStatusBar();
		try {

			// Text.
			String text = mainWindow.getToolBar().getText();
			if (text == null || text.length() == 0) {
				return;
			}

			// Search.
			int id = Integer.parseInt(text);
			Command command = corpusService.getCommand(id);
			if (command != null) {
				mainWindow.getCorpusTreeView().selectCommand(command);
			}

		} catch (Exception exception) {
			statusBar.setError(exception.getMessage());
		}
	}
}