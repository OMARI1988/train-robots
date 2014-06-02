/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.trainrobots.collections.ArrayIterator;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.MultiMap;
import com.trainrobots.scenes.Scene;

public class Commands implements Items<Command> {

	private final Command[] commands;
	private final Map<Integer, Command> commandById = new HashMap<>();
	private final MultiMap<Integer, Command> commandsBySceneId = new MultiMap<>();

	public Commands(Items<Command> commands) {
		this.commands = commands.toArray();
		for (Command command : commands) {
			commandById.put(command.id(), command);
			commandsBySceneId.add(command.scene().id(), command);
		}
	}

	@Override
	public int count() {
		return commands.length;
	}

	@Override
	public Command get(int index) {
		return commands[index];
	}

	public Command command(int id) {
		Command command = commandById.get(id);
		if (command == null) {
			throw new IllegalArgumentException(String.format(
					"The command ID '%d' is not recognized.", id));
		}
		return command;
	}

	public Items<Command> forScene(Scene scene) {
		return commandsBySceneId.get(scene.id());
	}

	@Override
	public Command[] toArray() {
		return commands.clone();
	}

	@Override
	public Iterator<Command> iterator() {
		return new ArrayIterator(commands);
	}
}