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
import javax.swing.JOptionPane;

import com.trainrobots.ui.services.CorpusService;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.MainWindow;

public class SaveAnnotationCommand extends AbstractAction {

	private final CorpusService corpusService;
	private final WindowService windowService;

	@Inject
	public SaveAnnotationCommand(CorpusService corpusService,
			WindowService windowService) {
		this.corpusService = corpusService;
		this.windowService = windowService;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		MainWindow window = windowService.getMainWindow();
		window.getCorpusView().update();
		corpusService.save();
		JOptionPane.showMessageDialog(window, "Annotations saved.");
	}
}