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

package com.trainrobots.core.corpus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.DataContext;
import com.trainrobots.core.io.FileReader;
import com.trainrobots.core.io.FileWriter;
import com.trainrobots.core.rcl.Rcl;

public class Corpus {

	private static List<Command> commands;
	private static Map<Integer, List<Command>> commandsByScene = new HashMap<Integer, List<Command>>();
	private static Map<Integer, List<Command>> accurateCommands = new HashMap<Integer, List<Command>>();
	private static Map<Integer, Command> commandsById = new HashMap<Integer, Command>();

	public static Command getCommand(int id) {
		loadCommands();
		return commandsById.get(id);
	}

	public static List<Command> getCommands() {
		loadCommands();
		return commands;
	}

	public static List<Command> getCommands(int sceneNumber) {
		loadCommands();
		return commandsByScene.get(sceneNumber);
	}

	public static List<Command> getAccurateCommands(int sceneNumber) {
		loadCommands();
		return accurateCommands.get(sceneNumber);
	}

	public static void saveAnnotation() {

		// Verify.
		loadCommands();

		// Annotations.
		FileWriter writer = new FileWriter(
				DataContext.getFile("annotation.txt"));
		for (Command command : commands) {
			if (command.mark != MarkType.Unmarked) {
				writer.writeLine(command.id + "\tmark\t"
						+ command.mark.getValue());
			}
			if (command.rcl != null) {
				writer.writeLine(command.id + "\trcl\t" + command.rcl);
			}
		}
		writer.close();

		// Enhancements.
		writer = new FileWriter(DataContext.getFile("enhancements.txt"));
		for (Command command : commands) {
			if (command.enhancement != 0) {
				writer.writeLine(command.id + "\t" + command.enhancement);
			}
		}
		writer.close();
	}

	private static void loadCommands() {
		if (commands != null) {
			return;
		}

		// Commands.
		commands = new ArrayList<Command>();
		FileReader reader = new FileReader(DataContext.getFile("commands.txt"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] items = line.split("\t");
			Command command = new Command();
			command.id = Integer.parseInt(items[0]);
			command.sceneNumber = Integer.parseInt(items[1]);
			command.text = items[2].trim();
			command.mark = MarkType.Unmarked;
			add(command);
		}
		reader.close();

		// Annotation.
		reader = new FileReader(DataContext.getFile("annotation.txt"));
		while ((line = reader.readLine()) != null) {
			String[] items = line.split("\t");
			Command command = commandsById.get(Integer.parseInt(items[0]));
			if (command == null) {
				throw new CoreException("Failed to find command ID " + items[0]);
			}
			if (items[1].equals("mark")) {
				command.mark = MarkType.getMark(Integer.parseInt(items[2]));
			} else if (items[1].equals("rcl")) {
				command.rcl = Rcl.fromString(items[2]);
				List<Command> list = accurateCommands.get(command.sceneNumber);
				if (list == null) {
					list = new ArrayList<Command>();
					accurateCommands.put(command.sceneNumber, list);
				}
				list.add(command);
			}
		}
		reader.close();

		// Enhancements.
		String path = DataContext.getFile("enhancements.txt");
		File file = new File(path);
		if (file.exists()) {
			reader = new FileReader(path);
			while ((line = reader.readLine()) != null) {
				String[] items = line.split("\t");
				Command command = commandsById.get(Integer.parseInt(items[0]));
				if (command == null) {
					throw new CoreException("Failed to find command ID "
							+ items[0]);
				}
				command.enhancement = Integer.parseInt(items[1]);
			}
			reader.close();
		}
	}

	private static void add(Command command) {

		// Commands.
		commands.add(command);

		// Commands by scene.
		List<Command> sceneCommands = commandsByScene.get(command.sceneNumber);
		if (sceneCommands == null) {
			sceneCommands = new ArrayList<Command>();
			commandsByScene.put(command.sceneNumber, sceneCommands);
		}
		sceneCommands.add(command);

		// Commands by ID.
		commandsById.put(command.id, command);
	}
}