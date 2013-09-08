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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.trainrobots.core.io.FileReader;
import com.trainrobots.nlp.scenes.SceneManager;

public class Corpus {

	private static List<Command> commands;
	private static Set<Integer> blackList = new HashSet<Integer>();

	public static List<Command> getCommands() {
		if (commands == null) {
			commands = new ArrayList<Command>();
			FileReader reader = new FileReader("../data/commands.txt");
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] items = line.split("\t");
				Command command = new Command();
				command.id = Integer.parseInt(items[0]);
				int sceneNumber = Integer.parseInt(items[1]);
				command.scene = SceneManager.getScene(sceneNumber);
				command.text = items[2].trim();
				if (!blackList.contains(command.id)) {
					commands.add(command);
				}
			}
			reader.close();
		}
		return commands;
	}

	static {
		blackList.add(3);
		blackList.add(11);
		blackList.add(16);
		blackList.add(530);
		blackList.add(533);
		blackList.add(536);
		blackList.add(539);
		blackList.add(542);
		blackList.add(545);
		blackList.add(548);
		blackList.add(551);
		blackList.add(554);
		blackList.add(557);
		blackList.add(560);
		blackList.add(563);
		blackList.add(672);
		blackList.add(3138);
		blackList.add(5231);
		blackList.add(5234);
		blackList.add(7522);
		blackList.add(9374);
	}
}