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

import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.AnnotationWindow;

public class SaveConfigurationCommand extends AbstractAction {

	private final WindowService windowService;

	@Inject
	public SaveConfigurationCommand(WindowService windowService) {
		this.windowService = windowService;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		AnnotationWindow window = windowService.get(AnnotationWindow.class);
		JOptionPane
				.showMessageDialog(window, "This command has been disabled.");
		// window.getSceneView().update();
		// configurationService.save();
		// JOptionPane.showMessageDialog(window, "Data saved successfully.");
	}
}