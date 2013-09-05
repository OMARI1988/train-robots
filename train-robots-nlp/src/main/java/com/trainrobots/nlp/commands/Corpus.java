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

package com.trainrobots.nlp.commands;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.nlp.io.FileReader;

public class Corpus {

	private static List<Command> commands;

	public static List<Command> getCommands() {
		if (commands == null) {
			commands = new ArrayList<Command>();
			FileReader reader = new FileReader("../data/commands.txt");
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] items = line.split("\t");
				Command command = new Command();
				command.id = Integer.parseInt(items[0]);
				command.sceneNumber = Integer.parseInt(items[1]);
				command.text = items[2].trim();
				commands.add(command);
			}
			reader.close();
		}
		return commands;
	}
}