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

package com.trainrobots.web.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.trainrobots.web.MersenneTwister;
import com.trainrobots.web.game.MarkedCommand;
import com.trainrobots.web.game.Scene;

public class GameService {

	private final List<Scene> addCommandScenes;
	private List<Scene> markedScenes;
	private final DataService dataService;
	private final MersenneTwister mersenneTwister = new MersenneTwister();

	public GameService(DataService dataService) {

		addCommandScenes = new ArrayList<Scene>();
		int x = 0;
		for (int g = 1; g <= 125; g++) {
			for (int c = 2; c <= 5; c++) {

				Scene sceneA = new Scene();
				sceneA.sceneNumber = ++x;
				sceneA.fromGroup = g;
				sceneA.toGroup = g;
				sceneA.fromImage = 1;
				sceneA.toImage = c;
				addCommandScenes.add(sceneA);

				Scene sceneB = new Scene();
				sceneB.sceneNumber = ++x;
				sceneB.fromGroup = g;
				sceneB.toGroup = g;
				sceneB.fromImage = c;
				sceneB.toImage = 1;
				addCommandScenes.add(sceneB);
			}
		}

		this.dataService = dataService;
	}

	public boolean isAddCommandRound(String email, int round) {

		// Admin?
		if (isAdmin(email)) {
			return false;
		}

		// Add command every third round.
		return round % 3 == 0;
	}

	public boolean isAdmin(String email) {
		return email != null && email.equals("kais@kaisdukes.com");
	}

	public synchronized Scene assignScene(ServletContext context, String email,
			int round) {

		// Admin?
		if (isAdmin(email)) {
			MarkedCommand command = dataService.getAdminCommand(context);
			if (command != null) {
				return translateMarkedCommand(command);
			}
		}

		// Add command?
		if (isAddCommandRound(email, round)) {
			return addCommandScenes.get(mersenneTwister.next(addCommandScenes
					.size()));
		}

		// Get marked commands.
		if (markedScenes == null) {
			markedScenes = new ArrayList<Scene>();
			for (MarkedCommand command : dataService.getMarkedCommands(context)) {
				markedScenes.add(translateMarkedCommand(command));
			}
		}
		return markedScenes.get(mersenneTwister.next(markedScenes.size()));
	}

	private Scene translateMarkedCommand(MarkedCommand command) {
		Scene scene = new Scene();
		scene.sceneNumber = command.sceneNumber;
		Scene s = addCommandScenes.get(command.sceneNumber - 1);
		scene.fromGroup = s.fromGroup;
		scene.toGroup = s.toGroup;
		scene.fromImage = s.fromImage;
		scene.toImage = s.toImage;
		scene.command = command.command;
		scene.expectedOption = command.commandMark;
		scene.rateUserId = command.userId;
		scene.rateRound = command.round;
		return scene;
	}
}