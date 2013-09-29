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

package com.trainrobots.ui.menus;

import javax.inject.Inject;
import javax.swing.JMenuBar;

import com.trainrobots.ui.commands.AlignCommand;
import com.trainrobots.ui.commands.ExitCommand;
import com.trainrobots.ui.commands.ParseCommand;
import com.trainrobots.ui.commands.SaveAnnotationCommand;
import com.trainrobots.ui.commands.ValidateCommand;

public class AnnotationMenu extends JMenuBar {

	@Inject
	public AnnotationMenu(final SaveAnnotationCommand saveAnnotationCommand,
			final ExitCommand exitCommand, final ParseCommand parseCommand,
			final AlignCommand alignCommand,
			final ValidateCommand validateCommand) {

		add(new Menu() {
			{
				// Initiate.
				setText("File");
				setMnemonic('F');

				// Items.
				addItem("Save Annotation", "control S", saveAnnotationCommand);
				addSeparator();
				addItem("Exit", "x", exitCommand);
			}
		});

		add(new Menu() {
			{
				// Initiate.
				setText("Annotation");
				setMnemonic('A');

				// Items.
				addItem("Parse", "control P", parseCommand);
				addItem("Align", "control N", alignCommand);
				addSeparator();
				addItem("Validate", "F5", validateCommand);
			}
		});
	}
}